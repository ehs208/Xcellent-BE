package com.leets.xcellentbe.domain.dm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.chatRoom.domain.repository.ChatRoomRepository;
import com.leets.xcellentbe.domain.chatRoom.exception.ChatRoomNotFoundException;
import com.leets.xcellentbe.domain.dm.domain.DM;
import com.leets.xcellentbe.domain.dm.domain.repository.DMRepository;
import com.leets.xcellentbe.domain.dm.dto.DMDto;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.domain.dm.exception.DMNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DMService {

	private final RedisTemplate<String, DMDto> redisTemplateMessage;
	private final ChatRoomRepository chatRoomRepository;
	private final UserRepository userRepository;
	private final DMRepository dmRepository;

	public void saveMessage(DMDto dmDto) {
		ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndDeletedStatusNot(dmDto.chatRoomId(), DeletedStatus.DELETED).orElseThrow(
			ChatRoomNotFoundException::new);

		User sender = userRepository.findById(dmDto.senderId()).orElseThrow(UserNotFoundException::new);
		User receiver = userRepository.findById(dmDto.receiverID()).orElseThrow(UserNotFoundException::new);
		String message = dmDto.message();

		DM dm = DM.create(sender, receiver, chatRoom, message);
		dmRepository.save(dm);

		chatRoom.updateLastMessage(message);
		chatRoomRepository.save(chatRoom);

		redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(DM.class));

		String receiverName = receiver.getUserName();

		redisTemplateMessage.opsForList().rightPush(receiverName, dmDto);

		redisTemplateMessage.expire(receiverName, 1, TimeUnit.MINUTES);
	}

	public List<DMDto> loadMessage(UUID chatRoomId) {
		List<DMDto> messageList = new ArrayList<>();
		ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndDeletedStatusNot(chatRoomId, DeletedStatus.DELETED).orElseThrow(ChatRoomNotFoundException::new);
		String receiverName = chatRoom.getReceiver().getUserName();

		List<DMDto> redisMessageList = redisTemplateMessage.opsForList().range(receiverName, 0, 99);

		if (redisMessageList == null || redisMessageList.isEmpty()) {
			List<DM> dbMessageList = dmRepository.findTop100ByChatRoomAndDeletedStatusNotOrderByCreatedAtAsc(chatRoom, DeletedStatus.DELETED);

			for (DM dm : dbMessageList) {
				DMDto messageDto = DMDto.from(dm);
				messageList.add(messageDto);
				redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(DM.class));
				redisTemplateMessage.opsForList().rightPush(receiverName, messageDto);
			}
		} else {
			messageList.addAll(redisMessageList);
		}

		return messageList;
	}

	public String deleteDM(UUID dmId) {
		DM dm = dmRepository.findById(dmId).orElseThrow(DMNotFoundException::new);

		dm.delete();
		dmRepository.save(dm);

		String receiverName = dm.getReceiver().getUserName();
		List<DMDto> redisMessageList = redisTemplateMessage.opsForList().range(receiverName, 0, -1);

		if (redisMessageList != null) {
			for (DMDto dmDto : redisMessageList) {
				if (dmDto.chatRoomId().equals(dm.getChatRoom().getChatRoomId()) &&
					dmDto.message().equals(dm.getMessage()) &&
					dmDto.senderId().equals(dm.getSender().getUserId()) &&
					dmDto.receiverID().equals(dm.getReceiver().getUserId())) {
					redisTemplateMessage.opsForList().remove(receiverName, 1, dmDto);
					break;
				}
			}
		}

		return "메시지를 삭제했습니다.";
	}
}

package com.leets.xcellentbe.domain.chatRoom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.chatRoom.domain.repository.ChatRoomRepository;
import com.leets.xcellentbe.domain.chatRoom.dto.ChatRoomDto;
import com.leets.xcellentbe.domain.chatRoom.exception.ChatRoomNotFoundException;
import com.leets.xcellentbe.domain.dm.domain.DM;
import com.leets.xcellentbe.domain.dm.domain.repository.DMRepository;
import com.leets.xcellentbe.domain.dm.dto.request.DMRequest;
import com.leets.xcellentbe.domain.dm.dto.response.DMResponse;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;
import com.leets.xcellentbe.global.redis.RedisSubscriber;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final DMRepository dmRepository;
	private final UserRepository userRepository;
	private final RedisMessageListenerContainer redisMessageListener;
	private final RedisSubscriber redisSubscriber;

	private static final String Message_Rooms = "MESSAGE_ROOM";
	private final RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, ChatRoomDto> opsHashMessageRoom;

	private Map<String, ChannelTopic> topics;

	@PostConstruct
	private void init() {
		opsHashMessageRoom = redisTemplate.opsForHash();
		topics = new HashMap<>();
	}

	public DMResponse createChatRoom(DMRequest dmRequest, User user) {
		User receiver = userRepository.findById(dmRequest.receiverId()).orElseThrow(UserNotFoundException::new);

		ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiverAndDeletedStatusNot(user, receiver, DeletedStatus.DELETED);

		if ((chatRoom == null) || (chatRoom != null && (!user.equals(chatRoom.getSender()) && !receiver.equals(
			chatRoom.getReceiver())))) {
			ChatRoomDto chatRoomDto = ChatRoomDto.of(dmRequest, user);
			opsHashMessageRoom.put(Message_Rooms, user.getUserName(), chatRoomDto);

			chatRoom = chatRoomRepository.save(ChatRoom.create(user, receiver));

			return new DMResponse(chatRoom);
		} else {
			return new DMResponse(chatRoom.getChatRoomId());
		}
	}

	public List<DMResponse> findAllChatRoomByUser(User user) {
		List<ChatRoom> chatRooms = chatRoomRepository.findBySenderOrReceiverAndDeletedStatusNot(user, user, DeletedStatus.DELETED);

		List<DMResponse> dmRespons = new ArrayList<>();

		for (ChatRoom chatRoom : chatRooms) {
			DMResponse messageRoomDto;
			DM latestMessage;

			if (user.equals(chatRoom.getSender())) {
				messageRoomDto = new DMResponse(
					chatRoom.getChatRoomId(),
					chatRoom.getSender(),
					chatRoom.getReceiver());
			} else {
				messageRoomDto = new DMResponse(
					chatRoom.getChatRoomId(),
					chatRoom.getSender(),
					chatRoom.getReceiver());
			}

			latestMessage = dmRepository.findTopByChatRoomAndDeletedStatusNotOrderByCreatedAtDesc(chatRoom, DeletedStatus.DELETED);

			if (latestMessage != null) {
				messageRoomDto.updateLatestMessageCreatedAt(latestMessage.getCreatedAt());
				messageRoomDto.updateLatestMessageContent(latestMessage.getMessage());
			}

			dmRespons.add(messageRoomDto);
		}

		return dmRespons;
	}

	public ChatRoomDto findChatRoom(UUID chatRoomId, User user) {
		ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndDeletedStatusNot(chatRoomId, DeletedStatus.DELETED).orElseThrow(ChatRoomNotFoundException::new);

		User receiver = chatRoom.getReceiver();

		chatRoom = chatRoomRepository.findByChatRoomIdAndSenderOrChatRoomIdAndReceiverAndDeletedStatusNot(chatRoomId, user,
			chatRoomId, receiver, DeletedStatus.DELETED).orElseThrow(ChatRoomNotFoundException::new);

		return ChatRoomDto.of(chatRoom.getChatRoomId(), chatRoom.getSender(), chatRoom.getReceiver());
	}

	public String deleteChatRoom(UUID chatRoomId, User user) {
		ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndSenderOrChatRoomIdAndReceiverAndDeletedStatusNot(chatRoomId, user,
			chatRoomId, user, DeletedStatus.DELETED).orElseThrow(ChatRoomNotFoundException::new);

		chatRoom.delete();
		chatRoomRepository.save(chatRoom);

		if (user.equals(chatRoom.getSender())) {
			opsHashMessageRoom.delete(Message_Rooms, chatRoom.getChatRoomId());
		}

		return "대화방을 삭제했습니다.";
	}

	public void enterChatRoom(Long receiverId) {
		String receiverName = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new).getUserName();
		ChannelTopic topic = topics.get(receiverName);

		if (topic == null) {
			topic = new ChannelTopic(receiverName);
			redisMessageListener.addMessageListener(redisSubscriber, topic);
			topics.put(receiverName, topic);
		}
	}

	public ChannelTopic getTopic(Long receiverId) {
		String receiverName = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new).getUserName();
		return topics.get(receiverName);
	}
}

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.chatRoom.domain.repository.ChatRoomRepository;
import com.leets.xcellentbe.domain.chatRoom.dto.ChatRoomDto;
import com.leets.xcellentbe.domain.chatRoom.exception.ChatRoomNotFoundException;
import com.leets.xcellentbe.domain.dm.domain.DM;
import com.leets.xcellentbe.domain.dm.domain.repository.DMRepository;
import com.leets.xcellentbe.domain.dm.dto.request.DMRequest;
import com.leets.xcellentbe.domain.dm.dto.response.DMResponse;
import com.leets.xcellentbe.domain.dm.redis.RedisSubscriber;
import com.leets.xcellentbe.domain.shared.DeletedStatus;
import com.leets.xcellentbe.domain.user.domain.User;
import com.leets.xcellentbe.domain.user.domain.repository.UserRepository;
import com.leets.xcellentbe.domain.user.exception.UserNotFoundException;

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

	public DMResponse createChatRoom(DMRequest dmRequest, UserDetails userDetails) {
		User sender = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);

		User receiver = userRepository.findById(dmRequest.receiverId()).orElseThrow(UserNotFoundException::new);

		ChatRoom chatRoom = chatRoomRepository.findBySenderAndReceiverAndDeletedStatusNot(sender, receiver,
			DeletedStatus.DELETED);

		if ((chatRoom == null) || (chatRoom != null && (!sender.equals(chatRoom.getSender()) && !receiver.equals(
			chatRoom.getReceiver())))) {
			ChatRoomDto chatRoomDto = ChatRoomDto.of(dmRequest, sender);
			opsHashMessageRoom.put(Message_Rooms, sender.getUserName(), chatRoomDto);

			chatRoom = chatRoomRepository.save(ChatRoom.create(sender, receiver));

			return new DMResponse(chatRoom);
		} else {
			return new DMResponse(chatRoom.getChatRoomId());
		}
	}

	public List<DMResponse> findAllChatRoomByUser(UserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);

		List<ChatRoom> chatRooms = chatRoomRepository.findBySenderOrReceiverAndDeletedStatusNot(user, user,
			DeletedStatus.DELETED);

		List<DMResponse> dmResponses = new ArrayList<>();

		for (ChatRoom chatRoom : chatRooms) {
			DMResponse messageRoomDto;

			messageRoomDto = new DMResponse(
				chatRoom.getChatRoomId(),
				chatRoom.getSender(),
				chatRoom.getReceiver());

			DM latestMessage = dmRepository.findTopByChatRoomAndDeletedStatusNotOrderByCreatedAtDesc(chatRoom,
				DeletedStatus.DELETED);

			if (latestMessage != null) {
				messageRoomDto.updateLatestMessageCreatedAt(latestMessage.getCreatedAt());
				messageRoomDto.updateLatestMessageContent(latestMessage.getMessage());
			}

			dmResponses.add(messageRoomDto);
		}

		return dmResponses;
	}

	public ChatRoomDto findChatRoom(UUID chatRoomId, UserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);

		ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndDeletedStatusNot(chatRoomId, DeletedStatus.DELETED)
			.orElseThrow(ChatRoomNotFoundException::new);

		User receiver = chatRoom.getReceiver();

		chatRoom = chatRoomRepository.findByChatRoomIdAndSenderOrChatRoomIdAndReceiverAndDeletedStatusNot(chatRoomId,
			user, chatRoomId, receiver, DeletedStatus.DELETED).orElseThrow(ChatRoomNotFoundException::new);

		return ChatRoomDto.of(chatRoom.getChatRoomId(), chatRoom.getSender(), chatRoom.getReceiver());
	}

	public String deleteChatRoom(UUID chatRoomId, UserDetails userDetails) {
		User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);

		ChatRoom chatRoom = chatRoomRepository.findByChatRoomIdAndSenderOrChatRoomIdAndReceiverAndDeletedStatusNot(
			chatRoomId, user, chatRoomId, user, DeletedStatus.DELETED).orElseThrow(ChatRoomNotFoundException::new);

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

package com.leets.xcellentbe.domain.dm.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.user.domain.User;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DMResponse {
	private UUID chatRoomId;
	private Long senderId;
	private Long receiverId;
	private String message;
	private LocalDateTime createdAt;

	@Builder
	private DMResponse(UUID chatRoomId, Long senderId, Long receiverId, String message, LocalDateTime createdAt) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.message = message;
		this.createdAt = createdAt;
	}

	public static DMResponse from(UUID chatRoomId) {
		return builder()
			.chatRoomId(chatRoomId)
			.build();
	}

	public static DMResponse from(ChatRoom chatRoom) {
		return builder()
			.chatRoomId(chatRoom.getChatRoomId())
			.senderId(chatRoom.getSender().getUserId())
			.receiverId(chatRoom.getReceiver().getUserId())
			.build();
	}

	public static DMResponse of(UUID chatRoomId, User sender, User receiver) {
		return builder()
			.chatRoomId(chatRoomId)
			.senderId(sender.getUserId())
			.receiverId(receiver.getUserId())
			.build();
	}

	public void updateLatestMessageContent(String message) {
		this.message = message;
	}

	public void updateLatestMessageCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}


package com.leets.xcellentbe.domain.dm.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leets.xcellentbe.domain.chatRoom.domain.ChatRoom;
import com.leets.xcellentbe.domain.user.domain.User;

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

	public DMResponse(ChatRoom chatRoom) {
		this.chatRoomId = chatRoom.getChatRoomId();
		this.senderId = chatRoom.getSender().getUserId();
		this.receiverId = chatRoom.getReceiver().getUserId();
	}

	public DMResponse(UUID chatRoomId, User sender, User receiver) {
		this.chatRoomId = chatRoomId;
		this.senderId = sender.getUserId();
		this.receiverId = receiver.getUserId();
	}

	public DMResponse(UUID chatRoomId) {
		this.chatRoomId = chatRoomId;
	}

	public void updateLatestMessageContent(String message) {
		this.message = message;
	}

	public void updateLatestMessageCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}


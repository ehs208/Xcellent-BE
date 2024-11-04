package com.leets.xcellentbe.domain.dm.dto;

import java.util.UUID;

import com.leets.xcellentbe.domain.dm.domain.DM;

public record DMDto(
	Long senderId,
	Long receiverID,
	UUID chatRoomId,
	String message) {

	public DMDto(DM dm) {
		this(dm.getSender().getUserId(), dm.getReceiver().getUserId(), dm.getChatRoom().getChatRoomId(),
			dm.getMessage());
	}

	public static DMDto from(DM dm) {
		return new DMDto(dm.getSender().getUserId(), dm.getReceiver().getUserId(), dm.getChatRoom().getChatRoomId(),
			dm.getMessage());
	}
}

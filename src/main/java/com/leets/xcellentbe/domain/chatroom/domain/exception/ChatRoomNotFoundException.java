package com.leets.xcellentbe.domain.chatroom.domain.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ChatRoomNotFoundException extends CommonException {
	public ChatRoomNotFoundException() {

		super(ErrorCode.CHAT_ROOM_NOT_FOUND);
	}
}

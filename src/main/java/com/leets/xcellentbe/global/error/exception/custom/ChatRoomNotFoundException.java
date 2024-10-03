package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ChatRoomNotFoundException extends CommonException {
	public ChatRoomNotFoundException(String message) {
		super(ErrorCode.CHAT_ROOM_NOT_FOUND);
	}
}

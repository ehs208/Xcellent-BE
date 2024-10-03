package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ChatRoomUnauthorizedException extends CommonException {
	public ChatRoomUnauthorizedException(String message) {
		super(ErrorCode.CHAT_ROOM_UNAUTHORIZED);
	}
}

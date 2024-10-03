package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ChatRoomUnauthorized extends CommonException {
	public ChatRoomUnauthorized(String message) {
		super(ErrorCode.CHAT_ROOM_UNAUTHORIZED);
	}
}

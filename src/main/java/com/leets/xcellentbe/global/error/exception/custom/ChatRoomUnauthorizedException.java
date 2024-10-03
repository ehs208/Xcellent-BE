package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ChatRoomUnauthorizedException extends CommonException {
	public ChatRoomUnauthorizedException() {
		super(ErrorCode.CHAT_ROOM_UNAUTHORIZED);
	}
}

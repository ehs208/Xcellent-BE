package com.leets.xcellentbe.domain.chatroom.domain.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ChatRoomForbiddenException extends CommonException {
	public ChatRoomForbiddenException() {

		super(ErrorCode.CHAT_ROOM_FORBIDDEN);
	}
}

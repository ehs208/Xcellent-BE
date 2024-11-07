package com.leets.xcellentbe.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "유효하지 않은 입력값입니다."),
	INVALID_FILE_FORMAT(400, "INVALID_FILE_FORMAT", "올바르지 않은 파일 형식입니다."),
	INVALID_TOKEN(401, "INVALID_TOKEN", "유효하지 않은 토큰입니다."),
	LOGIN_FAIL(401, "LOGIN_FAIL", "로그인에 실패하였습니다."),
	CHAT_ROOM_FORBIDDEN(403, "CHAT_ROOM_FORBIDDEN", "권한이 없는 채팅방입니다."),
	DELETE_FORBIDDEN(403, "DELETE_FORBIDDEN", "삭제 권한이 없습니다."),
	USER_NOT_FOUND(404, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),
	EXPIRED_TOKEN(403, "EXPIRED_TOKEN", "만료된 토큰입니다."),
	FOLLOW_OPERATION_ERROR(409, "FOLLOW_OPERATION_ERROR", "적절하지 않은 팔로우 요청입니다."),
	USER_ALREADY_EXISTS(412, "ALREADY_EXISTS_EXCEPTION", "이미 존재하는 사용자입니다."),
	ARTICLE_NOT_FOUND(404, "ARTICLE_NOT_FOUND", "게시물을 찾을 수 없습니다."),
	ARTICLE_MEDIA_NOT_FOUND(404, "ARTICLE_MEDIA_NOT_FOUND", "게시물 이미지를 찾을 수 없습니다."),
	CHAT_ROOM_NOT_FOUND(404, "CHAT_ROOM_NOT_FOUND", "채팅방을 찾을 수 없습니다."),
	DM_NOT_FOUND(404, "DM_NOT_FOUND", "메시지를 찾을 수 없습니다."),
	REJECT_DUPLICATION(409, "REJECT_DUPLICATION", "중복된 값입니다."),
	AUTH_CODE_ALREADY_SENT(429, "AUTH_CODE_ALREADY_SENT", "이미 인증번호를 전송했습니다."),
	INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),
	EMAIL_CANNOT_BE_SENT(500, "EMAIL_CANNOT_BE_SENT", "이메일 전송에 실패했습니다.");

	private final int status;
	private final String code;
	private final String message;
}

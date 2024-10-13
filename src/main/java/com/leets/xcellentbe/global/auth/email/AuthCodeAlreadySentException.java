package com.leets.xcellentbe.global.auth.email;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class AuthCodeAlreadySentException extends CommonException {
	public AuthCodeAlreadySentException() {
		super(ErrorCode.AUTH_CODE_ALREADY_SENT);
	}
}

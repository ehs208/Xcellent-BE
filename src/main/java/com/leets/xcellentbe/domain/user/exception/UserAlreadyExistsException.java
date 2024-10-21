package com.leets.xcellentbe.domain.user.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class UserAlreadyExistsException extends CommonException {
	public UserAlreadyExistsException() {
		super(ErrorCode.USER_ALREADY_EXISTS);
	}
}

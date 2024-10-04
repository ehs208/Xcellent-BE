package com.leets.xcellentbe.domain.user.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class UserNotFoundException extends CommonException {
	public UserNotFoundException() {

		super(ErrorCode.USER_NOT_FOUND);
	}
}

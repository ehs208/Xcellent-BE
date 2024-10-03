package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class UserNotFoundException extends CommonException {
	public UserNotFoundException(String message) {
		super(ErrorCode.USER_NOT_FOUND);
	}
}

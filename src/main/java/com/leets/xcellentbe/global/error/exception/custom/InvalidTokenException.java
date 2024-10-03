package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class InvalidTokenException extends CommonException {
	public InvalidTokenException(String message) {
		super(ErrorCode.INVALID_TOKEN);
	}
}

package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class InvalidInputValueException extends CommonException {
	public InvalidInputValueException() {
		super(ErrorCode.INVALID_INPUT_VALUE);
	}
}

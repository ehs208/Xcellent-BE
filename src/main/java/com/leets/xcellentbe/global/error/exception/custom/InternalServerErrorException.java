package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class InternalServerErrorException extends CommonException {
	public InternalServerErrorException() {

		super(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}

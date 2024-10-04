package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class ExpiredTokenException extends CommonException {
	public ExpiredTokenException() {

		super(ErrorCode.EXPIRED_TOKEN);
	}
}

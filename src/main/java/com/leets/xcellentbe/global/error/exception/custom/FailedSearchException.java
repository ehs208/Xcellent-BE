package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class FailedSearchException extends CommonException {
	public FailedSearchException(String message) {
		super(ErrorCode.FAILED_SEARCH);
	}
}

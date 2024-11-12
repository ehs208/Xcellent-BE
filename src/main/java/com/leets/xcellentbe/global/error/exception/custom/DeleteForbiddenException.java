package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class DeleteForbiddenException extends CommonException {
	public DeleteForbiddenException() {

		super(ErrorCode.DELETE_FORBIDDEN);
	}
}

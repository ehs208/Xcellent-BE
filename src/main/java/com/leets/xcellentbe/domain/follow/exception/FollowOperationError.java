package com.leets.xcellentbe.domain.follow.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class FollowOperationError extends CommonException {
	public FollowOperationError() {
		super(ErrorCode.FOLLOW_OPERATION_ERROR);
	}
}

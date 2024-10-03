package com.leets.xcellentbe.global.error.exception.custom;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class RejectDuplicationException extends CommonException {
	public RejectDuplicationException(String message) { super(ErrorCode.REJECT_DUPLICATION); }
}

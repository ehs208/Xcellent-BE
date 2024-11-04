package com.leets.xcellentbe.domain.dm.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class DMNotFoundException extends CommonException {

	public DMNotFoundException() {
		super(ErrorCode.DM_NOT_FOUND);
	}
}

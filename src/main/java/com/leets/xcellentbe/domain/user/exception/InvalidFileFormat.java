package com.leets.xcellentbe.domain.user.exception;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class InvalidFileFormat extends CommonException {
	public InvalidFileFormat() {
		super(ErrorCode.INVALID_FILE_FORMAT);
	}
}

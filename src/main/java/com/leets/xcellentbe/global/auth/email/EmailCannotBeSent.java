package com.leets.xcellentbe.global.auth.email;

import com.leets.xcellentbe.global.error.ErrorCode;
import com.leets.xcellentbe.global.error.exception.CommonException;

public class EmailCannotBeSent extends CommonException {
	public EmailCannotBeSent() {
		super(ErrorCode.EMAIL_CANNOT_BE_SENT);
	}
}

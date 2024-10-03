package com.leets.xcellentbe.global.error.exception;

import com.leets.xcellentbe.global.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonException extends RuntimeException {
	private final ErrorCode errorCode;
}

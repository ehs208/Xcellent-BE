package com.leets.xcellentbe.global.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.leets.xcellentbe.global.error.*;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CommonException.class) // Custom Exception을 포괄적으로 처리
	public ResponseEntity<ErrorResponse> handleCommonException(CommonException ex) {
		ErrorCode errorCode = ex.getErrorCode(); // 전달된 예외에서 에러 코드 가져오기
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

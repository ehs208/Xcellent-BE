package com.leets.xcellentbe.global.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.leets.xcellentbe.global.error.*;
import com.leets.xcellentbe.global.error.exception.custom.*;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ArticleNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ArticleNotFoundException ex) {
		ErrorCode errorCode = ErrorCode.ARTICLE_NOT_FOUND;
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FailedSearchException.class)
	public ResponseEntity<ErrorResponse> handleFailedSearchException(FailedSearchException ex) {
		ErrorCode errorCode = ErrorCode.FAILED_SEARCH;
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = new ErrorResponse(errorCode);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

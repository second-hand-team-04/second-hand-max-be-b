package com.codesquad.secondhand.common.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String message;

	public CustomException(ErrorType errorType) {
		super(errorType.getMessage());
		this.httpStatus = errorType.getHttpStatus();
		this.message = errorType.getMessage();
	}

	public int getCode() {
		return httpStatus.value();
	}

	public String getStatus() {
		return httpStatus.getReasonPhrase();
	}

	@Override
	public String getMessage() {
		return message;
	}
}

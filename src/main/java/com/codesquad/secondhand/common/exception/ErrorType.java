package com.codesquad.secondhand.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
	CURSOR_NOT_POSITIVE(HttpStatus.BAD_REQUEST, "커서는 정수만 가능합니다."),
	NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "숫자만 가능합니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ErrorType(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}

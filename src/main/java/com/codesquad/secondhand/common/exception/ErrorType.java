package com.codesquad.secondhand.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

	// USER
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),

	// REGION
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "숫자만 가능합니다."),

	// USER_REGION
	USER_REGION_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 등록된 동네가 있습니다."),
	USER_REGION_MAX_ADD_COUNT(HttpStatus.BAD_REQUEST, "동네 최대 등록 수를 초과하였습니다."),

	// COMMON
	NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "숫자만 가능합니다."),
	CURSOR_NOT_POSITIVE(HttpStatus.BAD_REQUEST, "커서는 정수만 가능합니다.");

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

package com.codesquad.secondhand.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

	// USER
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

	// REGION
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 동네입니다."),

	// USER_REGION
	USER_REGION_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 등록된 동네입니다."),
	USER_REGION_MAX_ADD_COUNT(HttpStatus.BAD_REQUEST, "동네 등록 수가 최대 제한을 초과했습니다. 동네 등록 요청이 거부되었습니다."),
	USER_REGION_MIN_REMOVE_COUNT(HttpStatus.BAD_REQUEST, "최소 1개의 동네는 필수입니다. 동네 삭제 요청이 거부되었습니다."),

	// IMAGE
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지가 없습니다.");

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

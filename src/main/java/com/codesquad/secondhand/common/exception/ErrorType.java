package com.codesquad.secondhand.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorType {

	// USER
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다"),
	USER_LOGIN_INFO_DIFFERENT(HttpStatus.BAD_REQUEST, "이메일이나 비밀번호가 다릅니다"),
	USER_EMAIL_PROVIDER_DUPLICATION(HttpStatus.BAD_REQUEST,  "이미 존재하는 이메일입니다"),
	USER_NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다"),

	// PROVIDER
	PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 공급자입니다"),

	// REGION
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 동네입니다"),

	// USER_REGION
	USER_REGION_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 등록된 동네입니다"),
	USER_REGION_MAX_ADD_COUNT(HttpStatus.BAD_REQUEST, "동네 등록 수가 최대 제한을 초과했습니다. 동네 등록 요청이 거부되었습니다"),
	USER_REGION_MIN_REMOVE_COUNT(HttpStatus.BAD_REQUEST, "최소 1개의 동네는 필수입니다. 동네 삭제 요청이 거부되었습니다"),

	// IMAGE
	IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지가 없습니다"),
	IMAGE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "해당 파일은 이미지 타입이 아닙니다"),
	IMAGE_EMPTY(HttpStatus.BAD_REQUEST, "해당 파일은 비어있거나 없습니다"),
	MAX_UPLOAD_SIZE(HttpStatus.BAD_REQUEST, "파일 업로드 사이즈가 초과하였습니다"),

	// AUTH
	AUTH_ACCESS_TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다"),
	AUTH_ACCESS_TOKEN_FORBIDDEN(HttpStatus.FORBIDDEN, "Access Token이 만료되었습니다"),
	AUTH_REFRESH_TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다"),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다"),

	// ITEM
	STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 판매 상태입니다"),
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"),
	ITEM_IMAGE_MAX_ADD_COUNT(HttpStatus.BAD_REQUEST, "상품 이미지 등록 수가 최대 제한을 초과했습니다. 상품 등록 요청이 거부되었습니다"),
	ITEM_IMAGE_EMPTY(HttpStatus.BAD_REQUEST, "이미지가 최소 1장 이상 포함되어야 합니다");

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

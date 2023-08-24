package com.codesquad.secondhand.common.response;

public enum ResponseMessage {

	CATEGORY_FIND_ALL("카테고리 목록 조회를 성공하였습니다."),
	REGION_FIND_ALL("동네 목록 조회를 성공하였습니다.");

	private final String message;

	ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

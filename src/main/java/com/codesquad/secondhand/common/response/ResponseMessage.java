package com.codesquad.secondhand.common.response;

public enum ResponseMessage {

	CATEGORY_FIND_ALL("카테고리 목록 조회를 성공하였습니다."),
	REGION_FIND_ALL("동네 목록 조회를 성공하였습니다."),
	MY_REGION_FIND_ALL("나의 동네 목록 조회를 성공하였습니다."),
	MY_REGION_ADD("나의 동네 등록을 성공하였습니다."),
	MY_REGION_REMOVE("나의 동네 삭제를 성공하였습니다."),
	NO_HANDLER_FOUND("해당 API 경로로는 요청할 수 없습니다."),
	ITEM_FIND_BY_REGION_AND_CATEGORY("상품 목록 조회를 성공하였습니다.");

	private final String message;

	ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

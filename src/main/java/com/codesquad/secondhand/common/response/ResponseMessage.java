package com.codesquad.secondhand.common.response;

public enum ResponseMessage {

	// category
	CATEGORY_FIND_ALL("카테고리 목록 조회를 성공하였습니다"),

	// region
	REGION_FIND_ALL("동네 목록 조회를 성공하였습니다"),
	MY_REGION_FIND_ALL("나의 동네 목록 조회를 성공하였습니다"),
	MY_REGION_ADD("나의 동네 등록을 성공하였습니다"),
	MY_REGION_REMOVE("나의 동네 삭제를 성공하였습니다"),
	MY_REGION_SELECTED("나의 동네 선택을 성공하였습니다"),

	// common
	NO_HANDLER_FOUND("해당 API 경로로는 요청할 수 없습니다"),
	JSON_IMPLEMENTATIONS_FAIL("잘못된 형식입니다"),

	// item
	ITEM_FIND_BY_REGION_AND_CATEGORY("상품 목록 조회를 성공하였습니다"),
	ITEM_CREATE("상품 등록을 성공하였습니다"),
	ITEM_DETAIL_FIND("상품 조회를 성공하였습니다"),
	ITEM_UPDATE_STATUS("상품 상태 수정을 성공하였습니다"),
	ITEM_UPDATE("상품 수정을 성공하였습니다"),
	ITEM_DELETE("상품 삭제를 성공하였습니다"),

	// account
	SIGN_IN("로그인을 성공하였습니다"),
	SIGN_OUT("로그아웃을 성공하였습니다"),
	UPDATE_PROFILE("사용자 정보 수정을 성공하였습니다"),
	ACCESS_TOKEN("Access Token 재발급을 성공하였습니다"),

	// user
	SIGN_UP("회원가입을 성공하였습니다."),
	USER_INFO("사용자 정보 조회를 성공하였습니다."),
	USER_FIND_MY_TRANSACTION_BY_STATUS("판매내역 조회를 성공하였습니다"),

	// image
	IMAGE_UPLOAD("이미지 등록을 성공하였습니다"),

	// wishlist
	WISHLIST_FIND("관심목록 조회를 성공하였습니다"),
	WISHLIST_ADD("관심목록 추가를 성공하였습니다"),
	WISHLIST_REMOVE("관심목록 삭제를 성공하였습니다"),
	WISHLIST_CATEGORIES("관심목록 카테고리 조회를 성공하였습니다");

	private final String message;

	ResponseMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

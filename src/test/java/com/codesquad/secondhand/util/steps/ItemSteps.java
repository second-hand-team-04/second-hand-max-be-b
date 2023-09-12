package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateRequest;
import com.codesquad.secondhand.item.application.dto.ItemUpdateStatusRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ItemSteps {

	public static ExtractableResponse<Response> 지역별_카테고리별_상품_목록_조회_요청(String accessToken, int page, int size, Long regionId, Long categoryId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/items?page={page}&size={size}&region={regionId}&category={categoryId}",
				page, size, regionId, categoryId)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_상세_조회_요청(String accessToken, long id) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/items/{id}", id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_생성_요청(String accessToken, ItemCreateRequest itemCreateRequest) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(itemCreateRequest)
			.when().post("/api/items")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_상태_수정_요청(String accessToken, long id, long statusId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new ItemUpdateStatusRequest(statusId))
			.when().patch("/api/items/{id}", id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_수정_요청(String accessToken, Long id, ItemUpdateRequest itemUpdateRequest) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(itemUpdateRequest)
			.when().put("/api/items/{id}", id)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_삭제_요청(String accessToken, long itemId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/items/{id}", itemId)
			.then().log().all()
			.extract();
	}
}

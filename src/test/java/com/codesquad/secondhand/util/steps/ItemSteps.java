package com.codesquad.secondhand.util.steps;

import java.util.List;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ItemSteps {

	public static ExtractableResponse<Response> 지역별_카테고리별_상품_목록_조회_요청(String accessToken, int page, int size,
		Long regionId, Long categoryId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/items?page={page}&size={size}&region={regionId}&category={categoryId}",
				page, size, regionId, categoryId)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 상품_생성_요청(String accessToken, String title, Integer price, String content, List<Long> imageIds, Long categoryId, Long regionID) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new ItemCreateRequest(title, price, content, imageIds, categoryId, regionID))
			.when().post("/api/items")
			.then().log().all()
			.extract();
	}
}

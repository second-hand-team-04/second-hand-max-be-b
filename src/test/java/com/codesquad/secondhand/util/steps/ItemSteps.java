package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ItemSteps {

	public static ExtractableResponse<Response> 지역별_카테고리별_상품_목록_조회_요청(int page, int size, long regionId,
		Long categoryId) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/items?page={page}&size={size}&region={regionId}&category={categoryId}",
				page, size, regionId, categoryId)
			.then().log().all()
			.extract();
	}

}

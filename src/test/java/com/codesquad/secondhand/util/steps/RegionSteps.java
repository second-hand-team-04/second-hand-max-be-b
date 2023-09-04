package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RegionSteps {

	public static ExtractableResponse<Response> 동네_목록_조회_요청(int page, int size, String title) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.param("page", page)
			.param("size", size)
			.param("title", title)
			.when().get("/api/regions")
			.then().log().all().extract();
	}
}

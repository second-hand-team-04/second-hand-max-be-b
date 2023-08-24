package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RegionSteps {

	public static ExtractableResponse<Response> 동네_목록_조회_요청(int cursor) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.param("cursor", cursor)
			.when().get("/api/regions")
			.then().log().all().extract();
	}
}

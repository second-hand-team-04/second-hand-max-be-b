package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CategorySteps {

	public static ExtractableResponse<Response> 카테고리_목록을_요청한다() {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/categories")
			.then().log().all().extract();
	}
}

package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.user.application.dto.UserRegionCreateRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class UserSteps {

	public static ExtractableResponse<Response> 나의_동네_목록_조회_요청() {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/users/regions")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 나의_동네_등록_요청(Long regionId) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new UserRegionCreateRequest(regionId))
			.when().post("/api/users/regions")
			.then().log().all().extract();
	}
}

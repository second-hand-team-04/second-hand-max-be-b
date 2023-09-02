package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

public class UserSteps {

	public static ExtractableResponse<Response> 유저_생성_요청(Long providerId, String email, String nickname, String password,
		MultiPartSpecification multiPartSpecification) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.multiPart("request", new UserCreateRequest(providerId, email, nickname, password), MediaType.APPLICATION_JSON_VALUE)
			.multiPart(multiPartSpecification)
			.when().post("/api/users")
			.then().log().all().extract();
	}

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
			.body(new UserRegionAddRequest(regionId))
			.when().post("/api/users/regions")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 나의_동네_삭제_요청(Long regionId) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/users/regions/{id}", regionId)
			.then().log().all().extract();
	}
}

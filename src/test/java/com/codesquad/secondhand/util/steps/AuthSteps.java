package com.codesquad.secondhand.util.steps;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.auth.application.dto.SignInRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthSteps {

	public static ExtractableResponse<Response> 로그인_요청(String email, String password) {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new SignInRequest(email, password))
			.when().post("/api/auth")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 로그아웃_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/auth")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 액세스_토큰_발급_요청(String refreshToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(refreshToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().post("/api/auth/refresh")
			.then().log().all().extract();
	}
}

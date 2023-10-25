package com.codesquad.secondhand.util.steps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.user.application.dto.UserCreateRequest;
import com.codesquad.secondhand.user.application.dto.UserRegionAddRequest;
import com.codesquad.secondhand.user.application.dto.UserUpdateRequest;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;

public class UserSteps {

	public static ExtractableResponse<Response> 유저_생성_요청(Long providerId, String email, String nickname,
		String password,
		MultiPartSpecification image) {
		RequestSpecification request = RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.multiPart(new MultiPartSpecBuilder(new UserCreateRequest(providerId, email, nickname, password),
				ObjectMapperType.JACKSON_2)
				.controlName("request")
				.mimeType(MediaType.APPLICATION_JSON_VALUE)
				.charset("UTF-8")
				.build());

		if (Objects.isNull(image)) {
			return request.when().post("/api/users")
				.then().log().all().extract();
		}

		return request.multiPart(image)
			.when().post("/api/users")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 유저_프로필_수정_요청(String accessToken, String nickname,
		boolean isImageChanged,
		MultiPartSpecification image) {
		RequestSpecification request = RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.multiPart(new MultiPartSpecBuilder(new UserUpdateRequest(nickname, isImageChanged),
				ObjectMapperType.JACKSON_2)
				.controlName("request")
				.mimeType(MediaType.APPLICATION_JSON_VALUE)
				.charset("UTF-8")
				.build());

		if (Objects.isNull(image)) {
			return request.when().patch("/api/users/info")
				.then().log().all().extract();
		}

		return request.multiPart(image)
			.when().patch("/api/users/info")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 나의_동네_목록_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/users/regions")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 나의_동네_등록_요청(String accessToken, Long regionId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new UserRegionAddRequest(regionId))
			.when().post("/api/users/regions")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 나의_동네_선택_요청(String accessToken, Long regionId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().patch("/api/users/regions/{id}", regionId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 나의_동네_삭제_요청(String accessToken, Long regionId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/users/regions/{id}", regionId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 유저_정보_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/users/info")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 유저_나의_판매내역_조회_요청(String accessToken, int page, int size,
		List<Long> statusIds) {
		Map<String, Object> params =
			Objects.nonNull(statusIds) ?
				Map.of("status", statusIds, "page", page, "size", size) :
				Map.of("page", page, "size", size);

		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.queryParams(params)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/users/transactions")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 관심_목록_등록_요청(String accessToken, Long itemId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new UserRegionAddRequest(itemId))
			.when().post("/api/users/wishlist/{itemId}", itemId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 관심_목록_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/users/wishlist")
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 관심_목록_삭제_요청(String accessToken, Long itemId) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().delete("/api/users/wishlist/{itemId}", itemId)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 관심_목록_카테고리_조회_요청(String accessToken) {
		return RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/users/wishlist/categories")
			.then().log().all().extract();
	}
}

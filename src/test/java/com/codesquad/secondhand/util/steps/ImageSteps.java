package com.codesquad.secondhand.util.steps;

import java.util.Objects;

import org.springframework.http.MediaType;

import com.codesquad.secondhand.image.application.dto.ImageUploadRequest;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;

public class ImageSteps {

	public static ExtractableResponse<Response> 이미지_업로드_요청(String accessToken, String type, MultiPartSpecification image) {
		RequestSpecification request = RestAssured.given().log().all()
			.auth().oauth2(accessToken)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.multiPart(new MultiPartSpecBuilder(new ImageUploadRequest(type),
				ObjectMapperType.JACKSON_2)
				.controlName("request")
				.mimeType(MediaType.APPLICATION_JSON_VALUE)
				.charset("UTF-8")
				.build());

		if (Objects.isNull(image)) {
			return request.when().post("/api/images")
				.then().log().all().extract();
		}

		return request.multiPart(image)
			.when().post("/api/images")
			.then().log().all().extract();
	}
}

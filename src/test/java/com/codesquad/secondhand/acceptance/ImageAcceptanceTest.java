package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.steps.ImageSteps.이미지_업로드_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.codesquad.secondhand.image.application.dto.ImageResponse;
import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

public class ImageAcceptanceTest extends AcceptanceTest {

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 이미지 파일을 업로드하면
	 * Then 업로드된 이미지 주소를 확인할 수 있다.
	 */
	@Test
	void 이미지를_업로드한다() throws IOException {
		// when
		var response = 이미지_업로드();

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		이미지_업로드_주소_검증(response, 1L, "http://www.image.com/상품.jpg");
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 비어있는 이미지 파일을 업로드하면
	 * Then 요청이 실패한다.
	 */
	@Test
	@NullSource
	void 파일이_없거나_비어있는_파일을_업로드하면_요청이_실패하다() {
		// when
		var response = 비어있는_이미지_파일을_업로드();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 이미지 타입이 아닌 파일을 업로드하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 이미지_타입이_아닌_파일을_업로드하면_요청이_실패한다() {
		// when
		var response = 이미지_타입이_아닌_파일을_업로드();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	private ExtractableResponse<Response> 이미지_업로드() throws IOException {
		MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
			.fileName(URLEncoder.encode("상품.jpg", StandardCharsets.UTF_8))
			.controlName("image")
			.mimeType(MediaType.IMAGE_JPEG_VALUE)
			.build();
		return 이미지_업로드_요청(유저_만두_액세스_토큰, "item", multiPartSpecification);
	}

	private void 이미지_업로드_주소_검증(ExtractableResponse<Response> response, Long id, String imageUrl) {
		ImageResponse actual = response.jsonPath().getObject("data", ImageResponse.class);

		Assertions.assertAll(
			() -> assertThat(actual.getId()).isEqualTo(id),
			() -> assertThat(actual.getImageUrl()).isEqualTo(imageUrl)
		);
	}

	private ExtractableResponse<Response> 비어있는_이미지_파일을_업로드() {
		MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder("".getBytes())
			.fileName(URLEncoder.encode("상품.jpg", StandardCharsets.UTF_8))
			.controlName("image")
			.mimeType(MediaType.IMAGE_JPEG_VALUE)
			.build();
		return 이미지_업로드_요청(유저_만두_액세스_토큰, "item", multiPartSpecification);
	}

	private ExtractableResponse<Response> 이미지_타입이_아닌_파일을_업로드() {
		MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder("".getBytes())
			.fileName(URLEncoder.encode("상품.jpg", StandardCharsets.UTF_8))
			.controlName("image")
			.mimeType(MediaType.APPLICATION_JSON_VALUE)
			.build();
		return 이미지_업로드_요청(유저_만두_액세스_토큰, "item", multiPartSpecification);
	}
}

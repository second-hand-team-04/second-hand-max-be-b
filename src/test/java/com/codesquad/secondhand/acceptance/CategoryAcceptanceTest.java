package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.steps.CategorySteps.카테고리_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.util.AcceptanceTest;
import com.codesquad.secondhand.util.fixture.CategoryFixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CategoryAcceptanceTest extends AcceptanceTest {

	/**
	 * Given 카테고리를 생성하고
	 * When 카테고리 목록을 조회하면
	 * Then 생성된 카테고리들을 조회할 수 있다.
	 */
	@Test
	void 카테고리_목록을_조회한다() {
		// when
		var response = 카테고리_목록_조회_요청(유저_만두_액세스_토큰);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		카테고리_목록을_조회하여_생성된_카테고리를_검증(response);
	}

	private void 카테고리_목록을_조회하여_생성된_카테고리를_검증(ExtractableResponse<Response> response) {
		List<String> titles = response.jsonPath().getList("data.title", String.class);
		List<String> imageUrls = response.jsonPath().getList("data.imageUrl", String.class);

		Assertions.assertAll(
			() -> assertThat(titles).containsExactly(CategoryFixture.getTitles().toArray(String[]::new)),
			() -> assertThat(imageUrls).containsExactly(CategoryFixture.getImageUrls().toArray(String[]::new)));
	}
}

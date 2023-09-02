package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.CategoryFixture.*;
import static com.codesquad.secondhand.util.fixture.ItemFixture.상품_PS5;
import static com.codesquad.secondhand.util.fixture.ItemFixture.상품_빈티지_일본_경대;
import static com.codesquad.secondhand.util.fixture.ItemFixture.상품_젤다의_전설;
import static com.codesquad.secondhand.util.fixture.RegionFixture.*;
import static com.codesquad.secondhand.util.steps.ItemSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ItemAcceptanceTest extends AcceptanceTest {

	/**
	 * Given 지역과 카테고리가 상이한 상품을 여러 개 생성하고
	 * When 지역별 카테고리별 상품을 조회하면
	 * Then 지역별 카테고리별 상품을 조회할 수 있다.
	 */
	@ParameterizedTest
	@MethodSource("providerCursorAndHasMoreAndRegion")
	void 전체_상품_목록을_조회한다(int page, int size, Long regionId, Long categoryId, boolean expectedHasMore,
		List<ItemResponse> expectedItemsResponse) {
		// when
		var response = 지역별_카테고리별_상품_목록_조회_요청(page, size, regionId, categoryId);

		//then
		응답_상태코드_검증(response, HttpStatus.OK);
		상품_목록_조회_시_생성된_상품을_검증(response, expectedHasMore, expectedItemsResponse);
	}

	public static Stream<Arguments> providerCursorAndHasMoreAndRegion() {
		return Stream.of(
			Arguments.of(
				0,
				2,
				동네_서울_종로구_청운동.getId(),
				카테고리_전체보기.getId(),
				true,
				List.of(
					상품_PS5.toItemResponse(),
					상품_젤다의_전설.toItemResponse()
				)
			),
			Arguments.of(
				1,
				2,
				동네_서울_종로구_청운동.getId(),
				카테고리_전체보기.getId(),
				false,
				List.of(
					상품_빈티지_일본_경대.toItemResponse()
				)
			)
		);
	}

	private void 상품_목록_조회_시_생성된_상품을_검증(ExtractableResponse<Response> response, boolean expectedHasMore,
		List<ItemResponse> expectedItemsResponse) {
		boolean actualHasMore = response.jsonPath().getBoolean("data.hasMore");
		List<ItemResponse> actualItems = response.jsonPath().getList("data.items", ItemResponse.class);

		Assertions.assertAll(
			() -> assertThat(actualHasMore).isEqualTo(expectedHasMore),
			() -> assertThat(actualItems).usingRecursiveComparison()
				.ignoringFields("id", "numChat")
				.isEqualTo(expectedItemsResponse)
		);
	}
}

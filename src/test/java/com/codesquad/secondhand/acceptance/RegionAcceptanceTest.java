package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.RegionFixture.*;
import static com.codesquad.secondhand.util.steps.RegionSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class RegionAcceptanceTest extends AcceptanceTest {

	/**
	 * Given 동네를 생성하고
	 * When 동네 목록을 조회하면
	 * Then 생성된 동네 목록을 조회할 수 있다.
	 */
	@ParameterizedTest
	@MethodSource("providerPageableAndRegion")
	void 동네_목록을_조회한다(int page, int size, boolean hasMore, String title, List<String> regionTitles) {
		// when
		var response = 동네_목록_조회_요청(유저_만두_액세스_토큰, page, size, title);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		동네_목록을_조회하여_생성된_동네를_검증(response, hasMore, regionTitles);
	}

	private static Stream<Arguments> providerPageableAndRegion() {
		return Stream.of(
			Arguments.of(
				1,
				5,
				true,
				"서울",
				List.of(
					동네_서울_종로구_누하동.getTitle(), 동네_서울_종로구_당주동.getTitle(), 동네_서울_종로구_도렴동.getTitle(),
					동네_서울_종로구_사직동.getTitle(), 동네_서울_종로구_세종로.getTitle()
				)
			),
			Arguments.of(
				0,
				20,
				false,
				"효자동",
				List.of(동네_서울_종로구_효자동.getTitle())
			)
		);
	}

	private void 동네_목록을_조회하여_생성된_동네를_검증(ExtractableResponse<Response> response, boolean expectedHasMore,
		List<String> expectedRegionTitles) {
		boolean actualHasMore = response.jsonPath().getBoolean("data.hasMore");
		List<String> actualTitles = response.jsonPath().getList("data.regions.title", String.class);

		Assertions.assertAll(
			() -> assertThat(actualHasMore).isEqualTo(expectedHasMore),
			() -> assertThat(actualTitles).containsExactly(expectedRegionTitles.toArray(String[]::new))
		);
	}
}

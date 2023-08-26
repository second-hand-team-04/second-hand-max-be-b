package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_궁정동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_내수동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_내자동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_누하동;
import static com.codesquad.secondhand.util.steps.UserSteps.나의_동네_등록_요청;
import static com.codesquad.secondhand.util.steps.UserSteps.나의_동네_목록_조회_요청;
import static com.codesquad.secondhand.util.steps.UserSteps.나의_동네_삭제_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class UserAcceptanceTest extends AcceptanceTest {

	/**
	 * Given 회원을 생성하고
	 * And 나의 동네를 2개를 등록하고
	 * When 나의 동네 목록을 조회하면
	 * Then 동네 목록을 조회할 수 있다.
	 */
	@Test
	void 나의_동네_목록을_조회한다() {
		// given
		나의_동네_등록_요청(동네_서울_종로구_궁정동.getId());
		나의_동네_등록_요청(동네_서울_종로구_내수동.getId());

		// when
		var response = 나의_동네_목록_조회_요청();

		// then
		나의_동네_목록_조회_검증(response, 동네_서울_종로구_궁정동.getTitle(), 동네_서울_종로구_내수동.getTitle());
	}

	/**
	 * Given 회원을 생성하고
	 * When 나의 동네를 등록하면
	 * Then 나의 동네 목록 조회 시 생성된 나의 동네를 조회할 수 있다.
	 */
	@Test
	void 나의_동네를_생성한다() {
		// when
		var response = 나의_동네_등록_요청(동네_서울_종로구_궁정동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
		나의_동네_목록_조회_시_등록된_나의_동네를_검증(동네_서울_종로구_궁정동.getTitle());
	}

	/**
	 * Given 회원을 생성하고
	 * When 나의 동네 등록 시 해당 동네가 없으면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_등록_시_해당_동네가_없으면_요청이_실패된다() {
		// when
		Long 생성되지_않은_동네_아이디 = 10000L;
		var response = 나의_동네_등록_요청(생성되지_않은_동네_아이디);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 회원을 생성하고
	 * And 나의 동네를 등록하고
	 * When 나의 동네 등록 시 이미 등록된 동네이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_등록_시_이미_등록된_동네이면_요청이_실패된다() {
		// given
		나의_동네_등록_요청(동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_등록_요청(동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 회원을 생성하고
	 * And 나의 동네 2개를 생성하고
	 * When 나의 동네 등록 시 이미 등록된 동네가 2개 이상이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_등록_시_이미_등록된_동네가_2개이면_요청이_실패된다() {
		// given
		나의_동네_등록_요청(동네_서울_종로구_내자동.getId());
		나의_동네_등록_요청(동네_서울_종로구_내수동.getId());

		// when
		var response = 나의_동네_등록_요청(동네_서울_종로구_누하동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * TODO 로그인 구현 시 테스트 코드 추가
	 *
	 * When 나의 동네 등록 시 해당 회원이 없으면
	 * Then 요청이 실패된다.
	 */

	/**
	 * Given 회원을 생성하고
	 * And 나의 동네를 2개를 등록하고
	 * When 나의 동네를 삭제하면
	 * Then 나의 동네 목록 조회 시 삭제된 나의 동네를 조회할 수 없다.
	 */
	@Test
	void 나의_동네를_삭제한다() {
		// given
		나의_동네_등록_요청(동네_서울_종로구_내자동.getId());
		나의_동네_등록_요청(동네_서울_종로구_내수동.getId());

		// when
		var response = 나의_동네_삭제_요청(동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.NO_CONTENT);
		나의_동네_목록_조회_시_삭제된_나의_동네를_검증(동네_서울_종로구_내자동.getTitle());
	}

	/**
	 * Given 회원을 생성하고
	 * And 나의 동네를 등록하고
	 * When 나의 동네를 삭제 시 나의 동네가 1개인 경우
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_삭제_시_나의_동네가_1개인_경우_요청이_실패된다() {
		// given
		나의_동네_등록_요청(동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_삭제_요청(동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * TODO 로그인 구현 시 테스트 코드 추가
	 *
	 * When 나의 동네 삭제 시 해당 회원이 없으면
	 * Then 요청이 실패된다.
	 */

	private void 나의_동네_목록_조회_검증(ExtractableResponse<Response> response, String... expectedTitles) {
		List<String> actualTitles = response.jsonPath().getList("data.title", String.class);

		assertThat(actualTitles).contains(expectedTitles);
	}

	private void 나의_동네_목록_조회_시_등록된_나의_동네를_검증(String regionTitle) {
		List<String> titles = 나의_동네_목록_조회_요청().jsonPath().getList("data.title", String.class);

		assertThat(titles).contains(regionTitle);
	}

	private void 나의_동네_목록_조회_시_삭제된_나의_동네를_검증(String regionTitle) {
		List<String> titles = 나의_동네_목록_조회_요청().jsonPath().getList("data.title", String.class);

		assertThat(titles).doesNotContain(regionTitle);
	}
}

package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.ProviderFixture.공급자_내부;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_강남구_역삼동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_궁정동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_내자동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_누하동;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_보노;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_지구;
import static com.codesquad.secondhand.util.steps.UserSteps.나의_동네_등록_요청;
import static com.codesquad.secondhand.util.steps.UserSteps.나의_동네_목록_조회_요청;
import static com.codesquad.secondhand.util.steps.UserSteps.나의_동네_삭제_요청;
import static com.codesquad.secondhand.util.steps.UserSteps.유저_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

public class UserAcceptanceTest extends AcceptanceTest {

	private static final String PROFILE_PATH = String.format("%s/%s", System.getProperty("user.dir"), "src/test/resources/bike.jpg");

	/**
	 * Given 회원을 생성하고
	 * And 나의 동네를 2개를 등록하고
	 * When 나의 동네 목록을 조회하면
	 * Then 동네 목록을 조회할 수 있다.
	 */
	@Test
	void 나의_동네_목록을_조회한다() {
		// given
		나의_동네_등록_요청(accessToken, 동네_서울_종로구_궁정동.getId());

		// when
		var response = 나의_동네_목록_조회_요청(accessToken);

		// then
		나의_동네_목록_조회_검증(response, 동네_서울_강남구_역삼동.getTitle(), 동네_서울_종로구_궁정동.getTitle());
	}

	/**
	 * Given 회원을 생성하고
	 * When 나의 동네를 등록하면
	 * Then 나의 동네 목록 조회 시 생성된 나의 동네를 조회할 수 있다.
	 */
	@Test
	void 나의_동네를_생성한다() {
		// when
		var response = 나의_동네_등록_요청(accessToken, 동네_서울_종로구_궁정동.getId());

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
		var response = 나의_동네_등록_요청(accessToken, 생성되지_않은_동네_아이디);

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
		// when
		var response = 나의_동네_등록_요청(accessToken, 동네_서울_강남구_역삼동.getId());

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
		나의_동네_등록_요청(accessToken, 동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_등록_요청(accessToken, 동네_서울_종로구_누하동.getId());

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
		나의_동네_등록_요청(accessToken, 동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_삭제_요청(accessToken, 동네_서울_종로구_내자동.getId());

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
	void 나의_동네_삭제_시_나의_동네가_1개인_경우_요청이_실패된다() {// when
		var response = 나의_동네_삭제_요청(accessToken, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * When 나의 동네 삭제 시 해당 회원이 없으면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_삭제_시_해당_회원이_없으면_요청이_실패된다() {
		// given
		String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6OTk5OTksImlzcyI6ImZpc2hwcmluY2Uuc2l0ZSIsImlhdCI6MTY5Mzg5MDY1OCwiZXhwIjoxNjk0ODkwNjU4fQ.qqooXsZoLtMaBilFjM2S2pA05srUn177gDqG80YKghs";

		// when
		var response = 나의_동네_삭제_요청(accessToken, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * When 유저를 생성하면
	 * Then 요청이 성공한다.
	 */
	@ParameterizedTest
	@MethodSource("providerUserAndMultiPartFile")
	void 유저를_생성한다(Long providerId, String email, String nickname, String password, MultiPartSpecification file) {
		// when
		var response = 유저_생성_요청(providerId, email, nickname, password, file);

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
	}

	/**
	 * When 유저를 생성 시 이메일 형식이 올바르지 않을 경우
	 * Then 요청이 실패된다.
	 */
	@Test
	void 유저_생성_시_이메일_형식이_올바르지_않을_경우_요청이_실패된다() {
		// when
		var response = 유저_생성_요청(공급자_내부.getId(), "mandu.com", 유저_보노.getNickname(), 유저_보노.getPassword(), null);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * When 유저를 생성 시 이미 존재하는 이메일인 경우
	 * Then 요청이 실패된다.
	 */
	@Test
	void 유저_생성_시_이미_존재하는_이메일인_경우_요청이_실패된다() {
		// when
		var response = 유저_생성_요청(공급자_내부.getId(), 유저_만두.getEmail(), 유저_보노.getNickname(), 유저_보노.getPassword(), null);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}


	/**
	 * When 유저를 생성 시 비밀번호가 8자 이상 16자 이하로, 영문, 숫자, 특수문자를 최소 1개씩 포함하지 않을 경우
	 * Then 요청이 실패된다.
	 */
	@ParameterizedTest
	@ValueSource(strings = {"bonotest", "bonotes1", "bonotes@", "1@2@3@4@", "bonobonotest1234!"})
	void 유저_생성_시_비밀번호가_8자_이상_16자_이하로_영문_숫자_특수문자를_최소_1개씩_포함하지_않을_경우_요청이_실패된다(String password) {
		// when
		var response = 유저_생성_요청(공급자_내부.getId(), 유저_보노.getEmail(), 유저_보노.getNickname(), password, null);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * When 유저를 생성 시 닉네임이 이미 중복된 경우
	 * Then 요청이 실패된다.
	 */
	@Test
	void 유저_생성_시_닉네임이_중복인_경우_요청이_실패된다() {
		// when
		var response = 유저_생성_요청(공급자_내부.getId(), 유저_보노.getEmail(), 유저_만두.getNickname(), 유저_보노.getPassword(), null);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	private static Stream<Arguments> providerUserAndMultiPartFile() throws FileNotFoundException {
		return Stream.of(
			Arguments.of(
				공급자_내부.getId(),
				유저_보노.getEmail(),
				유저_보노.getNickname(),
				유저_보노.getPassword(),
				new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
					.fileName("profile.jpg")
					.controlName("image")
					.mimeType(MediaType.IMAGE_JPEG_VALUE)
					.build()
			),
			Arguments.of(
				공급자_내부.getId(),
				유저_지구.getEmail(),
				유저_지구.getNickname(),
				유저_지구.getPassword(),
				null
			)
		);
	}

	private void 나의_동네_목록_조회_검증(ExtractableResponse<Response> response, String... expectedTitles) {
		List<String> actualTitles = response.jsonPath().getList("data.title", String.class);

		assertThat(actualTitles).contains(expectedTitles);
	}

	private void 나의_동네_목록_조회_시_등록된_나의_동네를_검증(String regionTitle) {
		List<String> titles = 나의_동네_목록_조회_요청(accessToken).jsonPath().getList("data.title", String.class);

		assertThat(titles).contains(regionTitle);
	}

	private void 나의_동네_목록_조회_시_삭제된_나의_동네를_검증(String regionTitle) {
		List<String> titles = 나의_동네_목록_조회_요청(accessToken).jsonPath().getList("data.title", String.class);

		assertThat(titles).doesNotContain(regionTitle);
	}
}

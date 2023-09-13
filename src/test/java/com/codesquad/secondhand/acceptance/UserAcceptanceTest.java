package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.ImageFixture.*;
import static com.codesquad.secondhand.util.fixture.ItemFixture.*;
import static com.codesquad.secondhand.util.fixture.ProviderFixture.*;
import static com.codesquad.secondhand.util.fixture.RegionFixture.*;
import static com.codesquad.secondhand.util.fixture.StatusFixture.*;
import static com.codesquad.secondhand.util.fixture.UserFixture.*;
import static com.codesquad.secondhand.util.steps.AuthSteps.*;
import static com.codesquad.secondhand.util.steps.ImageSteps.*;
import static com.codesquad.secondhand.util.steps.ItemSteps.*;
import static com.codesquad.secondhand.util.steps.UserSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
import com.codesquad.secondhand.item.application.dto.MyTransactionResponse;
import com.codesquad.secondhand.user.application.dto.UserInfoResponse;
import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

public class UserAcceptanceTest extends AcceptanceTest {

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 나의 동네를 2개를 등록하고
	 * When 나의 동네 목록을 조회하면
	 * Then 동네 목록을 조회할 수 있다.
	 */
	@Test
	void 나의_동네_목록을_조회한다() {
		// given
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_궁정동.getId());

		// when
		var response = 나의_동네_목록_조회_요청(유저_만두_액세스_토큰);

		// then
		나의_동네_목록_조회_검증(response, 동네_서울_강남구_역삼동.getId(), 동네_서울_강남구_역삼동.getTitle(), 동네_서울_종로구_궁정동.getTitle());
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 나의 동네를 등록하면
	 * Then 나의 동네 목록 조회 시 생성된 나의 동네를 조회할 수 있다.
	 */
	@Test
	void 나의_동네를_생성한다() {
		// when
		var response = 나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_궁정동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
		나의_동네_목록_조회_시_등록된_나의_동네를_검증(동네_서울_종로구_궁정동.getTitle());
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And   상품들을 생성하고
	 * When  나의 판매내역 조회 시
	 * Then  생성된 상품이 나의 판매내역에 조회된다.
	 * */
	@ParameterizedTest
	@MethodSource("providerPageableAndStatus")
	void 나의_판매내역을_조회한다(int page, int size, boolean expectedHasMore, List<Long> statusIds,
		List<MyTransactionResponse> expectedMyTransactionResponses) throws InterruptedException {
		// given
		나의_동네_청운동_궁정동_수정();
		상품들_생성_요청();

		// when
		var response = 유저_나의_판매내역_조회_요청(유저_만두_액세스_토큰, page, size, statusIds);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		나의_판매내역_조회_시_조회된_상품_검증(response, expectedHasMore, expectedMyTransactionResponses);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And   상품을 생성하고
	 * When  판매완료로 변경 시
	 * Then  판매완료로 변경된 상품이 나의 판매내역 조회의 판매완료 상태에 조회된다.
	 * */
	@Test
	void 판매완료_상태의_나의_판매내역을_조회한다() throws InterruptedException {
		// given
		상품_빈티지_일본_경대_생성();
		상품_상태_수정_요청(유저_만두_액세스_토큰, 1L, 2L);

		// when
		var response = 유저_나의_판매내역_조회_요청(유저_만두_액세스_토큰, 0, 10, List.of(2L));

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		나의_판매내역_조회_시_상품_조회_여부_검증(response, 1L);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 나의 동네 등록 시 해당 동네가 없으면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_등록_시_해당_동네가_없으면_요청이_실패된다() {
		// when
		Long 생성되지_않은_동네_아이디 = 10000L;
		var response = 나의_동네_등록_요청(유저_만두_액세스_토큰, 생성되지_않은_동네_아이디);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 나의 동네를 등록하고
	 * When 나의 동네 등록 시 이미 등록된 동네이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_등록_시_이미_등록된_동네이면_요청이_실패된다() {
		// when
		var response = 나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_강남구_역삼동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 나의 동네 2개를 생성하고
	 * When 나의 동네 등록 시 이미 등록된 동네가 2개 이상이면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_등록_시_이미_등록된_동네가_2개이면_요청이_실패된다() {
		// given
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_누하동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 나의 동네를 등록하고
	 * When 등록된 나의 동네를 선택하면
	 * Then 나의 동네 목록 조회 시 선택된 나의 동네를 확인할 수 있다.
	 */
	@Test
	void 나의_동네를_선택한다() {
		// given
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_선택_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		나의_동네_목록_조회_검증(나의_동네_목록_조회_요청(유저_만두_액세스_토큰), 동네_서울_종로구_내자동.getId(),
			동네_서울_강남구_역삼동.getTitle(), 동네_서울_종로구_내자동.getTitle());
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 존재하지 않는 동네로 나의 동네를 선택하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_동네로_동네를_선택하면_요청이_실패된다() {
		// when
		var response = 나의_동네_선택_요청(유저_만두_액세스_토큰, 99999L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 나의 동네에 등록되지 않는 동네로 나의 동네를 선택하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 나의_동네에_등록되지_않은_동네로_나의_동네를_선택하면_요청이_실패된다() {
		// when
		var response = 나의_동네_선택_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 나의 동네를 2개를 등록하고
	 * When 나의 동네를 삭제하면
	 * Then 나의 동네 목록 조회 시 삭제된 나의 동네를 조회할 수 없다.
	 */
	@Test
	void 나의_동네를_삭제한다() {
		// given
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// when
		var response = 나의_동네_삭제_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		나의_동네_목록_조회_시_삭제된_나의_동네를_검증(동네_서울_종로구_내자동.getTitle());
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 나의 동네를 등록하고
	 * When 나의 동네를 삭제 시 나의 동네가 1개인 경우
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_삭제_시_나의_동네가_1개인_경우_요청이_실패된다() {// when
		var response = 나의_동네_삭제_요청(유저_만두_액세스_토큰, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 나의 동네 삭제 시 해당 유저이 없으면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 나의_동네_삭제_시_해당_유저가_없으면_요청이_실패된다() {
		// given
		String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6OTk5OTksImlzcyI6ImZpc2hwcmluY2Uuc2l0ZSIsImlhdCI6MTY5Mzg5MDY1OCwiZXhwIjoxNjk0ODkwNjU4fQ.qqooXsZoLtMaBilFjM2S2pA05srUn177gDqG80YKghs";

		// when
		var response = 나의_동네_삭제_요청(accessToken, 동네_서울_종로구_내자동.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 유저를 생성하면
	 * Then 요청이 성공한다.
	 */
	@ParameterizedTest
	@MethodSource("providerUserAndImage")
	void 유저를_생성한다(Long providerId, String email, String nickname, String password, MultiPartSpecification file) {
		// when
		var response = 유저_생성_요청(providerId, email, nickname, password, file);

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
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
	 * Given 동네들, 카테고리들, 유저를 생성하고
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
	 * Given 동네들, 카테고리들, 유저를 생성하고
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
	 * Given 동네들, 카테고리들, 유저를 생성하고
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

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * And 	 로그인을 하고
	 * When  유저 정보를 조회하면
	 * Then  유저 정보를 조회할 수 있다.
	 * */
	@ParameterizedTest
	@MethodSource("providerUserAndImage")
	void 유저_정보를_조회한다(Long providerId, String email, String nickname, String password, MultiPartSpecification image,
		String expectedImageUrl) {
		// given
		유저_생성_요청(providerId, email, nickname, password, image);
		String accessToken = 로그인_요청(email, password).jsonPath().getString("data.accessToken");

		// when
		var response = 유저_정보_조회_요청(accessToken);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		유저_정보_조회_검증(response, 2L, nickname, expectedImageUrl);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 프로필을 수정하면
	 * Then 요청이 성공한다.
	 */
	@ParameterizedTest
	@MethodSource("providerNicknameAndImage")
	void 유저_프로필을_수정한다(String nickname, boolean isImageChanged, MultiPartSpecification image,
		String expectedImageUrl) throws
		FileNotFoundException {
		// given
		String accessToken = 유저_보노_생성_및_로그인_요청();

		// when
		var response = 유저_프로필_수정_요청(accessToken, nickname, isImageChanged, image);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		유저_정보_조회_검증(유저_정보_조회_요청(accessToken), 유저_보노.getId(), nickname, expectedImageUrl);
	}

	/**
	 * Given 동네들, 카테고리들, 유저를 생성하고
	 * When 프로필 수정 시 닉네임이 중복인 경우
	 * Then 요청이 실패된다.
	 */
	@Test
	void 유저_프로필_수정_시_닉네임이_중복인_경우_요청이_실패된다() {
		// given
		유저_생성_요청(공급자_내부.getId(), 유저_보노.getEmail(), 유저_보노.getNickname(), 유저_보노.getPassword(), null);

		// when
		var response = 유저_프로필_수정_요청(유저_만두_액세스_토큰, 유저_보노.getNickname(), false, null);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * When 관심 목록을 조회하면
	 * Then 요청이 성공한다.
	 * */
	@Test
	void 관심_목록을_조회한다() {
		// when
		var response = 관심_목록_조회_요청(유저_만두_액세스_토큰);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
	}

	/**
	 * Given 상품을 생성하고
	 * And   상품을 삭제하고
	 * When  관심 목록을 조회하면
	 * Then  삭제된 상품은 조회되지 않는다.
	 */
	@Test
	void 관심_목록을_조회하면_삭제된_상품은_조회되지_않는다() {
		// given
		상품_빈티지_일본_경대_생성();
		상품_삭제_요청(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_조회_요청(유저_만두_액세스_토큰);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		관심_목록_조회_시_상품이_관심_목록에_없음을_검증(유저_만두_액세스_토큰, 1L);
	}

	/**
	 * Given 상품을 관심 목록에 등록하고
	 * When  관심 목록을 조회하면
	 * Then  관심 목록에 추가되고
	 * And   상품의 관심 수가 1 증가한다.
	 * */
	@Test
	void 상품을_관심_목록에_등록한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
		관심_목록_조회_시_관심_등록된_상품_검증(유저_만두_액세스_토큰, 1L);
		상품_상세_조회_시_관심_등록_여부_검증(유저_만두_액세스_토큰, 1L, 1, true);
	}

	/**
	 * Given 상품이 존재하지 않을 때
	 * When  상품을 관심 목록에 등록하면
	 * Then  요청이 실패한다.
	 * */
	@Test
	void 존재하지_않는_상품을_관심_목록에_등록하면_요청이_실패한다() {
		// given
		상품_상세_조회_시_상품이_존재하지_않음을_검증(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 상품을 생성하고
	 * And   상품을 관심 목록에 등록하고
	 * When  같은 상품을 관심 목록에 등록하면
	 * Then  요청이 실패한다.
	 * */
	@Test
	void 이미_관심_목록에_등록된_상품을_관심_목록에_등록하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);
		관심_목록_조회_시_관심_등록된_상품_검증(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 상품을 생성하고
	 * And   상품을 삭제했을 때
	 * When  삭제된 상품을 관심 목록에 등록하면
	 * Then  요청이 실패한다.
	 * */
	@Test
	void 삭제된_상품을_관심_목록에_등록하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		상품_삭제_요청(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 상품을 생성하고
	 * And   상품을 관심 목록에 등록하고
	 * When  상품을 관심 목록에서 삭제하면
	 * Then	 나의 관심 목록에서 조회되지 않고
	 * And   상품의 관심 수가 1 감소한다.
	 * */
	@Test
	void 상품을_관심_목록에서_삭제한다() {
		// given
		상품_빈티지_일본_경대_생성();
		관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_삭제_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		관심_목록_조회_시_상품이_관심_목록에_없음을_검증(유저_만두_액세스_토큰, 1L);
		상품_상세_조회_시_관심_등록_여부_검증(유저_만두_액세스_토큰, 1L, 0, false);
	}

	/**
	 * Given 상품이 존재하지 않을 때
	 * When  상품을 관심 목록에서 삭제 요청하면
	 * Then  요청이 실패한다.
	 * */
	@Test
	void 존재하지_않는_상품을_관심_목록에서_삭제하면_요청이_실패한다() {
		// given
		상품_상세_조회_시_상품이_존재하지_않음을_검증(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_삭제_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 상품이 존재하고
	 * And   상품이 관심 목록에 등록되어 있지 않을 때
	 * When  상품을 관심 목록에서 삭제 요청하면
	 * Then  요청이 실패한다.
	 * */
	@Test
	void 관심_목록에_등록되지_않은_상품을_관심_목록에서_삭제하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		관심_목록_조회_시_상품이_관심_목록에_없음을_검증(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_삭제_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 상품을 생성하고
	 * And   상품을 삭제했을 때
	 * When  삭제된 상품을 관심 목록에 등록 요청하면
	 * Then  요청이 실패한다.
	 * */
	@Test
	void 삭제된_상품을_관심_목록에서_삭제_요청하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		상품_삭제_요청(유저_만두_액세스_토큰, 1L);

		// when
		var response = 관심_목록_등록_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	private static Stream<Arguments> providerNicknameAndImage() throws FileNotFoundException {
		return Stream.of(
			Arguments.of(
				"보노보노",
				true,
				new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
					.fileName("profile.jpg")
					.controlName("image")
					.mimeType(MediaType.IMAGE_JPEG_VALUE)
					.build(),
				"http://www.image.com/profile.jpg"
			),
			Arguments.of(
				유저_보노.getNickname(),
				true,
				new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
					.fileName("profile.jpg")
					.controlName("image")
					.mimeType(MediaType.IMAGE_JPEG_VALUE)
					.build(),
				"http://www.image.com/profile.jpg"
			),
			Arguments.of(
				유저_보노.getNickname(),
				false,
				new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
					.fileName("profile.jpg")
					.controlName("image")
					.mimeType(MediaType.IMAGE_JPEG_VALUE)
					.build(),
				"http://www.image.com/bono.jpg"
			),
			Arguments.of(
				"보노보노",
				true,
				null,
				null
			),
			Arguments.of(
				유저_보노.getNickname(),
				false,
				null,
				"http://www.image.com/bono.jpg"
			)
		);
	}

	private static Stream<Arguments> providerUserAndImage() throws FileNotFoundException {
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
					.build(),
				"http://www.image.com/profile.jpg"
			),
			Arguments.of(
				공급자_내부.getId(),
				유저_지구.getEmail(),
				유저_지구.getNickname(),
				유저_지구.getPassword(),
				null,
				null
			)
		);
	}

	private static Stream<Arguments> providerPageableAndStatus() {
		return Stream.of(
			Arguments.of(
				0,
				10,
				false,
				List.of(판매중.getId(), 예약중.getId()),
				List.of(
					상품_삼천리_자전거.toMyTransactionResponse(null),
					상품_코렐_접시.toMyTransactionResponse(null),
					상품_젤다의_전설.toMyTransactionResponse(null),
					상품_PS5.toMyTransactionResponse(null),
					상품_빈티지_일본_경대.toMyTransactionResponse(이미지_빈티지_일본_경대.getImageUrl()))
			),
			Arguments.of(
				0,
				2,
				true,
				List.of(판매중.getId(), 예약중.getId()),
				List.of(
					상품_삼천리_자전거.toMyTransactionResponse(null),
					상품_코렐_접시.toMyTransactionResponse(null))
			),
			Arguments.of(
				0,
				10,
				false,
				null,
				List.of(
					상품_삼천리_자전거.toMyTransactionResponse(null),
					상품_코렐_접시.toMyTransactionResponse(null),
					상품_젤다의_전설.toMyTransactionResponse(null),
					상품_PS5.toMyTransactionResponse(null),
					상품_빈티지_일본_경대.toMyTransactionResponse(이미지_빈티지_일본_경대.getImageUrl()))
			)
		);
	}

	private void 나의_동네_목록_조회_검증(ExtractableResponse<Response> response, Long expectedSelectedId,
		String... expectedTitles) {
		Long actualSelectedId = response.jsonPath().getLong("data.selectedId");
		List<String> actualTitles = response.jsonPath().getList("data.regions.title", String.class);

		assertThat(actualSelectedId).isEqualTo(expectedSelectedId);
		assertThat(actualTitles).contains(expectedTitles);
	}

	private void 나의_동네_목록_조회_시_등록된_나의_동네를_검증(String regionTitle) {
		List<String> titles = 나의_동네_목록_조회_요청(유저_만두_액세스_토큰).jsonPath().getList("data.regions.title", String.class);

		assertThat(titles).contains(regionTitle);
	}

	private void 나의_동네_목록_조회_시_삭제된_나의_동네를_검증(String regionTitle) {
		List<String> titles = 나의_동네_목록_조회_요청(유저_만두_액세스_토큰).jsonPath().getList("data.regions.title", String.class);

		assertThat(titles).doesNotContain(regionTitle);
	}

	private void 유저_정보_조회_검증(ExtractableResponse<Response> response, Long expectedId, String expectedNickname,
		String expectedImageUrl) {
		UserInfoResponse actualResponse = response.jsonPath().getObject("data", UserInfoResponse.class);

		Assertions.assertAll(
			() -> assertThat(actualResponse.getUserId()).isEqualTo(expectedId),
			() -> assertThat(actualResponse.getNickname()).isEqualTo(expectedNickname),
			() -> assertThat(actualResponse.getImageUrl()).isEqualTo(expectedImageUrl)
		);
	}

	private String 유저_보노_생성_및_로그인_요청() throws FileNotFoundException {
		유저_생성_요청(공급자_내부.getId(), 유저_보노.getEmail(), 유저_보노.getNickname(), 유저_보노.getPassword(),
			new MultiPartSpecBuilder(new FileInputStream(PROFILE_PATH))
				.fileName("bono.jpg")
				.controlName("image")
				.mimeType(MediaType.IMAGE_JPEG_VALUE)
				.build());
		return 로그인_요청(유저_보노.getEmail(), 유저_보노.getPassword()).jsonPath().getString("data.accessToken");
	}

	private void 상품들_생성_요청() throws InterruptedException {
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_빈티지_일본_경대.getFileName()));
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_빈티지_일본_경대2.getFileName()));
		상품_생성_요청(유저_만두_액세스_토큰, new ItemCreateRequest(
			상품_빈티지_일본_경대.getTitle(), 상품_빈티지_일본_경대.getPrice(),
			상품_빈티지_일본_경대.getContent(), List.of(이미지_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대2.getId()),
			상품_빈티지_일본_경대.getCategoryId(), 상품_빈티지_일본_경대.getRegionId()));
		Thread.sleep(1000);
		상품_생성_요청(유저_만두_액세스_토큰, new ItemCreateRequest(상품_PS5.getTitle(), 상품_PS5.getPrice(),
			상품_PS5.getContent(), null, 상품_PS5.getCategoryId(), 상품_PS5.getRegionId()));
		Thread.sleep(1000);
		상품_생성_요청(유저_만두_액세스_토큰, new ItemCreateRequest(상품_젤다의_전설.getTitle(), 상품_젤다의_전설.getPrice(),
			상품_젤다의_전설.getContent(), null, 상품_젤다의_전설.getCategoryId(), 상품_젤다의_전설.getRegionId()));
		Thread.sleep(1000);
		상품_생성_요청(유저_만두_액세스_토큰, new ItemCreateRequest(상품_코렐_접시.getTitle(), 상품_코렐_접시.getPrice(),
			상품_코렐_접시.getContent(), null, 상품_코렐_접시.getCategoryId(), 상품_코렐_접시.getRegionId()));
		Thread.sleep(1000);
		상품_생성_요청(유저_만두_액세스_토큰, new ItemCreateRequest(상품_삼천리_자전거.getTitle(), 상품_삼천리_자전거.getPrice(),
			상품_삼천리_자전거.getContent(), null, 상품_삼천리_자전거.getCategoryId(), 상품_삼천리_자전거.getRegionId()));
	}

	private void 상품_빈티지_일본_경대_생성() {
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_빈티지_일본_경대.getFileName()));
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_빈티지_일본_경대2.getFileName()));
		long regionId = 나의_동네_목록_조회_요청(유저_만두_액세스_토큰).jsonPath()
			.getLong("data.selectedId");
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_빈티지_일본_경대.getTitle(), 상품_빈티지_일본_경대.getPrice(),
			상품_빈티지_일본_경대.getContent(), List.of(이미지_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대2.getId()),
			상품_빈티지_일본_경대.getCategoryId(), regionId);

		상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private void 나의_동네_청운동_궁정동_수정() {
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_청운동.getId());
		나의_동네_삭제_요청(유저_만두_액세스_토큰, 동네_서울_강남구_역삼동.getId());
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_궁정동.getId());
	}

	private void 나의_판매내역_조회_시_조회된_상품_검증(ExtractableResponse<Response> response, boolean expectedHasMore,
		List<MyTransactionResponse> expectedMyTransactionResponses) {
		boolean actualHasMore = response.jsonPath().getBoolean("data.hasMore");
		List<MyTransactionResponse> actualTransactionResponses = response.jsonPath()
			.getList("data.items", MyTransactionResponse.class);

		Assertions.assertAll(
			() -> assertThat(actualHasMore).isEqualTo(expectedHasMore),
			() -> assertThat(actualTransactionResponses).usingRecursiveComparison()
				.ignoringFields("updatedAt")
				.isEqualTo(expectedMyTransactionResponses)
		);
	}

	private void 나의_판매내역_조회_시_상품_조회_여부_검증(ExtractableResponse<Response> response,
		Long... expectedIds) {
		boolean actualHasMore = response.jsonPath().getBoolean("data.hasMore");
		List<Long> actual = response.jsonPath()
			.getList("data.items.id", Long.class);

		assertThat(actual).contains(expectedIds);
	}

	private void 관심_목록_조회_시_상품이_관심_목록에_없음을_검증(String accessToken, Long... expectedItemIds) {
		List<Long> actualItemIds = 관심_목록_조회_요청(accessToken).jsonPath().getList("data.items.id", Long.class);

		assertThat(actualItemIds).doesNotContain(expectedItemIds);
	}

	private void 상품_상세_조회_시_상품이_존재하지_않음을_검증(String accessToken, Long... expectedItemId) {
		var actual = 상품_상세_조회_요청(accessToken, 1L);

		응답_상태코드_검증(actual, HttpStatus.NOT_FOUND);
	}

	private void 상품_상세_조회_시_관심_등록_여부_검증(String accessToken, Long itemId, int expectedNumLikes,
		boolean expectedIsLiked) {
		ItemDetailResponse actual = 상품_상세_조회_요청(accessToken, itemId).jsonPath()
			.getObject("data", ItemDetailResponse.class);

		Assertions.assertAll(
			() -> assertThat(actual.getNumLikes()).isEqualTo(expectedNumLikes),
			() -> assertThat(actual.getIsLiked()).isEqualTo(expectedIsLiked)
		);
	}

	private void 관심_목록_조회_시_관심_등록된_상품_검증(String accessToken, Long... expectedItemIds) {
		List<Long> actualItemIds = 관심_목록_조회_요청(accessToken).jsonPath().getList("data.items.id", Long.class);

		assertThat(actualItemIds).contains(expectedItemIds);
	}
}

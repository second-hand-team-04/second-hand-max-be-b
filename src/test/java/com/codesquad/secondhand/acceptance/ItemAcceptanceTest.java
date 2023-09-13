package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.CategoryFixture.*;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.common.exception.ErrorType;
import com.codesquad.secondhand.image.application.dto.ImageResponse;
import com.codesquad.secondhand.item.application.dto.ItemCreateRequest;
import com.codesquad.secondhand.item.application.dto.ItemDetailResponse;
import com.codesquad.secondhand.item.application.dto.ItemResponse;
import com.codesquad.secondhand.item.application.dto.ItemUpdateRequest;
import com.codesquad.secondhand.util.AcceptanceTest;
import com.codesquad.secondhand.util.fixture.CategoryFixture;
import com.codesquad.secondhand.util.fixture.ImageFixture;
import com.codesquad.secondhand.util.fixture.StatusFixture;
import com.codesquad.secondhand.util.fixture.UserFixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ItemAcceptanceTest extends AcceptanceTest {

	@BeforeEach
	void init() {
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_빈티지_일본_경대.getFileName()));
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_빈티지_일본_경대2.getFileName()));
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_도자기_화병_5종.getFileName()));
		이미지_업로드_요청(유저_만두_액세스_토큰, "item", createFile(이미지_잎사귀_포스터.getFileName()));
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_청운동.getId());
		나의_동네_삭제_요청(유저_만두_액세스_토큰, 동네_서울_강남구_역삼동.getId());
		나의_동네_등록_요청(유저_만두_액세스_토큰, 동네_서울_종로구_궁정동.getId());
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 지역과 카테고리가 상이한 상품을 여러 개 생성하고
	 * When 지역별 카테고리별 상품을 조회하면
	 * Then 지역별 카테고리별 상품을 조회할 수 있다.
	 */
	@ParameterizedTest
	@MethodSource("providerPageableAndRegion")
	void 전체_상품_목록을_조회한다(int page, int size, boolean expectedHasMore, Long regionId, Long categoryId,
		List<ItemResponse> expectedItemsResponse) throws InterruptedException {
		// given
		상품들_생성_요청();

		// when
		var response = 지역별_카테고리별_상품_목록_조회_요청(유저_만두_액세스_토큰, page, size, regionId, categoryId);

		//then
		응답_상태코드_검증(response, HttpStatus.OK);
		상품_목록_조회_시_생성된_상품을_검증(response, expectedHasMore, expectedItemsResponse);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And  상품을 생성하고
	 * When 상품 상세 조회하면
	 * Then 생성된 상품을 상세 조회 할 수 있다.
	 */
	@Test
	void 상품을_상세_조회한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 상품_상세_조회_요청(유저_만두_액세스_토큰, 상품_빈티지_일본_경대.getId());

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		상품_상세_조회_시_생성된_상품을_검증한다(response);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * When  상품을 생성하면
	 * Then  요청이 성공한다.
	 */
	@ParameterizedTest
	@MethodSource("providerValidItem")
	void 상품을_생성한다(String title, Integer price, String content, List<Long> imageIds, Long categoryId, Long regionId) {
		// when
		var response = 상품_생성(title, price, content, imageIds, categoryId, regionId);

		// then
		응답_상태코드_검증(response, HttpStatus.CREATED);
		상품_상세_조회_시_생성된_상품을_검증한다(title, price, content, imageIds, categoryId);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * When 상품 생성 시 이미지가 10개 이상이면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_이미지가_10개_이상이면_요청이_실패한다() {
		// when
		var response = 이미지가_10개_이상_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * When 상품 생성 시 업로드 되어 있지 않은 이미지이면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_업로드_되어_있지_않은_이미지일_경우_요청이_실패한다() {
		// when
		var response = 업로드_되어_있지_않은_이미지_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 동네가 없으면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_동네가_없으면_요청이_실패한다() {
		// when
		var response = 동네가_없는_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 존재하지 않는 동네이면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_존재하지_않는_동네이면_요청이_실패한다() {
		// when
		var response = 동네가_존재하지_않는_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 *  When 상품 생성 시 카테고리가 없으면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_카테고리가_없으면_요청이_실패한다() {
		// when
		var response = 카테고리가_비어있는_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 존재하지 않는 카테고리면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_존재하지_않는_카테고리면_요청이_실패한다() {
		// when
		var response = 카테고리가_존재_하지_않는_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 제목이 없으면
	 *  Then 요청이 실패한다.
	 */
	@ParameterizedTest
	@MethodSource("providerItemNoTitle")
	void 상품_생성_시_제목이_없으면_요청이_실패한다(String title, Integer price, String content, List<Long> imageIds, Long categoryId,
		Long regionId) {
		// when
		var response = 상품_생성(title, price, content, imageIds, categoryId, regionId);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 제목이 60자를 초과하면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_제목이_60자를_초과하면_요청이_실패한다() {
		// when
		var response = 제목이_60자를_초과하는_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 내용이 없으면
	 *  Then 요청이 실패한다.
	 */
	@ParameterizedTest
	@MethodSource("providerItemNoContents")
	void 상품_생성_시_내용이_없으면_요청이_실패한다(String title, Integer price, String content, List<Long> imageIds, Long categoryId,
		Long regionId) {
		// when
		var response = 상품_생성(title, price, content, imageIds, categoryId, regionId);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 상품 생성 시 내용이 3000자를 넘으면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 상품_생성_시_내용이_3000자를_초과하면_요청이_실패한다() {
		// when
		var response = 내용이_3000자를_초과하는_상품_생성();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들, 상품을 생성하고
	 *  When 상품 상태를 수정하면
	 *  Then 상품 상세 조회 시 상태를 확인 할 수 있다.
	 */
	@Test
	void 상품_상태를_수정한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 상품_상태_수정_요청(유저_만두_액세스_토큰, 1L, 2L);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		상품_상세_조회_시_상품_상태를_검증(1L, 2);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들, 상품을 생성하고
	 *  And 다른 유저를 생성 후 로그인하고
	 *  When 본인이 등록한 판매 상품인 아닌 상품 상태를 수정하면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 본인이_등록한_판매_상품인_아닌_상품_상태를_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		String 유저_보노_액세스_토큰 = 유저_보노_생성_후_로그인();

		// when
		var response = 상품_상태_수정_요청(유저_보노_액세스_토큰, 1L, 2L);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들, 상품을 생성하고
	 *  And 다른 유저를 생성 후 로그인하고
	 *  When 존재하지 않는 상태로 상품 상태를 수정하면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_상태로_상품_상태를_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 상품_상태_수정_요청(유저_만두_액세스_토큰, 1L, 9999L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 *  Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 *  When 존재하지 않는 상품의 상태를 수정하면
	 *  Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_상품의_상태를_수정하면_요청이_실패한다() {
		// when
		var response = 상품_상태_수정_요청(유저_만두_액세스_토큰, 1L, 2L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 상품을 생성하고
	 * When 상품을 삭제하면
	 * Then 상품 상세 조회 시 조회 할 수가 없다.
	 */
	@Test
	void 상품을_삭제한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 상품_삭제_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		상품_상세_조회_시_삭제된_상품을_검증(1L);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * When 존재하지 않는 상품을 삭제하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_상품을_삭제하면_요청이_실패한다() {
		// when
		var response = 상품_삭제_요청(유저_만두_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 다른 유저를 생성 후 로그인하고
	 * And 상품을 생성하고
	 * When 본인이 등록한 판매 상품인 아닌 상품을 삭제하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 본인이_등록한_판매_상품이_아닌_상품을_삭제하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		String 유저_보노_액세스_토큰 = 유저_보노_생성_후_로그인();

		// when
		var response = 상품_삭제_요청(유저_보노_액세스_토큰, 1L);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 상풀을 생성하고
	 * When 상품을 수정하면
	 * Then 상품 상세 조회 시 수정된 부분을 확인할 수 있다.
	 */
	@ParameterizedTest
	@MethodSource("providerUpdatedItem")
	void 상품을_수정한다(String title, Integer price, String content, List<Long> imageIds, Long categoryId, Long regionId) {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 상품_수정(title, price, content, imageIds, categoryId, regionId);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		상품_상세_조회_시_수정된_상품을_검증한다(title, price, content, imageIds, categoryId);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 다른 유저를 생성 후 로그인하고
	 * And 상품을 생성하고
	 * When 본인이 등록한 판매 상품인 아닌 상품을 수정하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 본인이_등록한_판매_상품인_상품을_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();
		String 유저_보노_액세스_토큰 = 유저_보노_생성_후_로그인();

		// when
		var response = 본인이_등록한_판매_상품인_상품_수정(유저_보노_액세스_토큰);

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * When 존재하지 않는 상품을 수정하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_상품을_수정하면_요청이_실패한다() {
		// when
		ExtractableResponse<Response> response = 존재하지_않는_상품으로_상품_수정();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 상풀을 생성하고
	 * When 존재하지 않는 카테고리로 상품을 수정하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_카테고리로_상품을_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 존재하지_않는_카테고리로_상품_수정();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 상풀을 생성하고
	 * When 존재하지 않는 동네로 상품을 수정하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_동네로_상품을_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		ExtractableResponse<Response> response = 존재하지_않는_동네로_상품_수정();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 상풀을 생성하고
	 * When 나의 동네에 등록되지 않은 동네로 상품을 수정하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 나의_동네에_등록되지_않은_동네로_상품을_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 나의_동네에_등록되지_않는_동네로_상품_수정();

		// then
		응답_상태코드_검증(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Given 동네들, 카테고리들, 유저, 이미지들을 생성하고
	 * And 상풀을 생성하고
	 * When 존재하지 않는 이미지로 상품을 수정하면
	 * Then 요청이 실패한다.
	 */
	@Test
	void 존재하지_않는_이미지로_상품을_수정하면_요청이_실패한다() {
		// given
		상품_빈티지_일본_경대_생성();

		// when
		var response = 존재하지_않는_이미지로_상품_수정();

		// then
		응답_상태코드_검증(response, HttpStatus.NOT_FOUND);
	}

	public static Stream<Arguments> providerPageableAndRegion() {
		return Stream.of(
			Arguments.of(
				0,
				2,
				true,
				동네_서울_종로구_청운동.getId(),
				카테고리_전체보기.getId(),
				List.of(
					상품_젤다의_전설.toItemResponse(null, 0, 0),
					상품_PS5.toItemResponse(null, 0, 0)
				)
			),
			Arguments.of(
				1,
				2,
				false,
				동네_서울_종로구_청운동.getId(),
				카테고리_전체보기.getId(),
				List.of(
					상품_빈티지_일본_경대.toItemResponse(이미지_빈티지_일본_경대.getImageUrl(), 0, 0)
				)
			),
			Arguments.of(
				0,
				2,
				false,
				동네_서울_종로구_청운동.getId(),
				카테고리_게임_취미.getId(),
				List.of(
					상품_젤다의_전설.toItemResponse(null, 0, 0),
					상품_PS5.toItemResponse(null, 0, 0)
				)
			)
		);
	}

	private static Stream<Arguments> providerValidItem() {
		return Stream.of(
			Arguments.of(
				상품_PS5.getTitle(),
				상품_PS5.getPrice(),
				상품_PS5.getContent(),
				LongStream.range(1, 11)
					.map(n -> 이미지_도자기_화병_5종.getId())
					.boxed().collect(Collectors.toUnmodifiableList()),
				상품_PS5.getCategoryId(),
				상품_PS5.getRegionId()
			),
			Arguments.of(
				상품_PS5.getTitle(),
				null,
				상품_PS5.getContent(),
				LongStream.range(1, 11)
					.map(n -> 이미지_도자기_화병_5종.getId())
					.boxed().collect(Collectors.toUnmodifiableList()),
				상품_PS5.getCategoryId(),
				상품_PS5.getRegionId()
			)
		);
	}

	private static Stream<Arguments> providerItemNoTitle() {
		return Stream.of(
			Arguments.of(
				null,
				상품_PS5.getPrice(),
				상품_PS5.getContent(),
				List.of(이미지_잎사귀_포스터.getId()),
				상품_PS5.getCategoryId(),
				상품_PS5.getRegionId()
			),
			Arguments.of(
				" ".repeat(60),
				상품_PS5.getPrice(),
				상품_PS5.getContent(),
				List.of(이미지_잎사귀_포스터.getId()),
				상품_PS5.getCategoryId(),
				상품_PS5.getRegionId()
			)
		);
	}

	private static Stream<Arguments> providerItemNoContents() {
		return Stream.of(
			Arguments.of(
				상품_PS5.getTitle(),
				상품_PS5.getPrice(),
				null,
				List.of(이미지_잎사귀_포스터.getId()),
				상품_PS5.getCategoryId(),
				상품_PS5.getRegionId()
			),
			Arguments.of(
				상품_PS5.getTitle(),
				상품_PS5.getPrice(),
				" ".repeat(3000),
				List.of(이미지_잎사귀_포스터.getId()),
				상품_PS5.getCategoryId(),
				상품_PS5.getRegionId()
			)
		);
	}

	private static Stream<Arguments> providerUpdatedItem() {
		return Stream.of(
			Arguments.of(
				상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), null,
				상품_PS5.getCategoryId(), 상품_PS5.getRegionId()
			),
			Arguments.of(
				상품_PS5.getTitle(), null, 상품_PS5.getContent(),
				List.of(이미지_빈티지_일본_경대.getId()), 상품_PS5.getCategoryId(), 상품_PS5.getRegionId()
			),
			Arguments.of(
				상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(),
				List.of(이미지_빈티지_일본_경대.getId(), 이미지_잎사귀_포스터.getId()), 상품_PS5.getCategoryId(), 상품_PS5.getRegionId()
			)
		);
	}

	private void 상품들_생성_요청() throws InterruptedException {
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

	private void 상품_목록_조회_시_생성된_상품을_검증(ExtractableResponse<Response> response, boolean expectedHasMore,
		List<ItemResponse> expectedItemsResponse) {
		boolean actualHasMore = response.jsonPath().getBoolean("data.hasMore");
		List<ItemResponse> actualItems = response.jsonPath().getList("data.items", ItemResponse.class);

		Assertions.assertAll(
			() -> assertThat(actualHasMore).isEqualTo(expectedHasMore),
			() -> assertThat(actualItems).usingRecursiveComparison()
				.ignoringFields("createdAt", "updatedAt", "numChat")
				.isEqualTo(expectedItemsResponse)
		);
	}

	private void 상품_상세_조회_시_생성된_상품을_검증(ExtractableResponse<Response> response, ItemDetailResponse expected) {
		ItemDetailResponse actual = response.jsonPath().getObject("data", ItemDetailResponse.class);

		assertThat(actual).usingRecursiveComparison()
			.ignoringFields("updatedAt")
			.isEqualTo(expected);
	}

	private void 상품_빈티지_일본_경대_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_빈티지_일본_경대.getTitle(), 상품_빈티지_일본_경대.getPrice(),
			상품_빈티지_일본_경대.getContent(), List.of(이미지_빈티지_일본_경대.getId(), 이미지_빈티지_일본_경대2.getId()),
			상품_빈티지_일본_경대.getCategoryId(), 상품_빈티지_일본_경대.getRegionId());
		상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private void 상품_상세_조회_시_생성된_상품을_검증한다(ExtractableResponse<Response> response) {
		List<ImageResponse> expectedImage = List.of(이미지_빈티지_일본_경대.toImageResponse(), 이미지_빈티지_일본_경대2.toImageResponse());
		상품_상세_조회_시_생성된_상품을_검증(response, 상품_빈티지_일본_경대.toItemDetailResponse(1, 0, 0, expectedImage));
	}

	private ExtractableResponse<Response> 상품_생성(String title, Integer price, String content, List<Long> imageIds,
		Long categoryId, Long regionId) {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(title, price, content, imageIds, categoryId,
			regionId);
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private void 상품_상세_조회_시_생성된_상품을_검증한다(String title, Integer price, String content, List<Long> imageIds,
		Long categoryId) {
		ItemDetailResponse expected = new ItemDetailResponse(1L, title, content, price, 0, 0, 1,
			false, null, 판매중.toStatusItemDetailResponse(),
			CategoryFixture.findCategoryItemDetailResponseById(categoryId),
			UserFixture.findUserItemDetailResponseById(유저_만두.getId()),
			ImageFixture.findAllImageResponseByIds(imageIds));

		상품_상세_조회_시_생성된_상품을_검증(상품_상세_조회_요청(유저_만두_액세스_토큰, 1), expected);
	}

	private ExtractableResponse<Response> 이미지가_10개_이상_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, 상품_PS5.getContent(),
			List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L),
			카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId());
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 업로드_되어_있지_않은_이미지_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, 상품_PS5.getContent(),
			List.of(9999L),
			카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId());
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 동네가_없는_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, 상품_PS5.getContent(),
			List.of(이미지_잎사귀_포스터.getId()),
			카테고리_게임_취미.getId(), null);
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 동네가_존재하지_않는_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, 상품_PS5.getContent(),
			List.of(이미지_잎사귀_포스터.getId()),
			카테고리_게임_취미.getId(), 9999L);
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 카테고리가_존재_하지_않는_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, 상품_PS5.getContent(),
			List.of(이미지_잎사귀_포스터.getId()),
			Long.valueOf(CategoryFixture.values().length + 1), 동네_서울_종로구_청운동.getId());
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 카테고리가_비어있는_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, 상품_PS5.getContent(),
			List.of(이미지_잎사귀_포스터.getId()),
			null, 동네_서울_종로구_청운동.getId());
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 제목이_60자를_초과하는_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest("a".repeat(61),
			null, 상품_PS5.getContent(), List.of(이미지_잎사귀_포스터.getId()),
			카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId());
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private ExtractableResponse<Response> 내용이_3000자를_초과하는_상품_생성() {
		ItemCreateRequest itemCreateRequest = new ItemCreateRequest(상품_PS5.getTitle(), null, "a".repeat(3001),
			List.of(이미지_잎사귀_포스터.getId()), 카테고리_게임_취미.getId(), 동네_서울_종로구_청운동.getId());
		return 상품_생성_요청(유저_만두_액세스_토큰, itemCreateRequest);
	}

	private void 상품_상세_조회_시_상품_상태를_검증(long itemId, long statusId) {
		String actual = 상품_상세_조회_요청(유저_만두_액세스_토큰, itemId).jsonPath().getString("data.status");

		assertThat(actual).isEqualTo(StatusFixture.findById(statusId).getType());
	}

	private String 유저_보노_생성_후_로그인() {
		유저_생성_요청(공급자_내부.getId(), 유저_보노.getEmail(), 유저_보노.getNickname(), 유저_보노.getPassword(), null);
		return 로그인_요청(유저_보노.getEmail(), 유저_보노.getPassword()).jsonPath().getString("data.accessToken");
	}

	private void 상품_상세_조회_시_삭제된_상품을_검증(long itemId) {
		var response = 상품_상세_조회_요청(유저_만두_액세스_토큰, itemId);
		String message = response.jsonPath().getString("message");

		Assertions.assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
			() -> assertThat(message).isEqualTo(ErrorType.ITEM_NOT_FOUND.getMessage())
		);
	}

	private ExtractableResponse<Response> 본인이_등록한_판매_상품인_상품_수정(String accessToken) {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(
			상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), List.of(이미지_도자기_화병_5종.getId()),
			상품_PS5.getCategoryId(), 상품_PS5.getRegionId());
		return 상품_수정_요청(accessToken, 1L, itemUpdateRequest);
	}

	private void 상품_상세_조회_시_수정된_상품을_검증한다(String title, Integer price, String content, List<Long> imageIds,
		Long categoryId) {
		ItemDetailResponse itemDetailResponse = new ItemDetailResponse(
			1L, title, content, price, 0, 0, 1, false, null,
			판매중.toStatusItemDetailResponse(), CategoryFixture.findCategoryItemDetailResponseById(categoryId),
			유저_만두.toUserItemDetailResponse(), ImageFixture.findAllImageResponseByIds(imageIds)
		);

		상품_상세_조회_시_생성된_상품을_검증(상품_상세_조회_요청(유저_만두_액세스_토큰, 1L), itemDetailResponse);
	}

	private ExtractableResponse<Response> 상품_수정(String title, Integer price, String content, List<Long> imageIds,
		Long categoryId, Long regionId) {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(title, price, content, imageIds, categoryId,
			regionId);
		return 상품_수정_요청(유저_만두_액세스_토큰, 1L, itemUpdateRequest);
	}

	private ExtractableResponse<Response> 존재하지_않는_상품으로_상품_수정() {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(
			상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), List.of(이미지_도자기_화병_5종.getId()),
			상품_PS5.getCategoryId(), 상품_PS5.getRegionId());
		return 상품_수정_요청(유저_만두_액세스_토큰, 1L, itemUpdateRequest);
	}

	private ExtractableResponse<Response> 존재하지_않는_카테고리로_상품_수정() {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(
			상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), List.of(이미지_도자기_화병_5종.getId()),
			99999L, 상품_PS5.getRegionId());
		return 상품_수정_요청(유저_만두_액세스_토큰, 1L, itemUpdateRequest);
	}

	private ExtractableResponse<Response> 존재하지_않는_동네로_상품_수정() {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(
			상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), List.of(이미지_도자기_화병_5종.getId()),
			상품_PS5.getCategoryId(), 9999L);
		return 상품_수정_요청(유저_만두_액세스_토큰, 1L, itemUpdateRequest);
	}

	private ExtractableResponse<Response> 나의_동네에_등록되지_않는_동네로_상품_수정() {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(
			상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), List.of(이미지_도자기_화병_5종.getId()),
			상품_PS5.getCategoryId(), 동네_서울_종로구_누하동.getId());
		var response = 상품_수정_요청(유저_만두_액세스_토큰, 1L, itemUpdateRequest);
		return response;
	}

	private ExtractableResponse<Response> 존재하지_않는_이미지로_상품_수정() {
		ItemUpdateRequest itemUpdateRequest = new ItemUpdateRequest(
			상품_PS5.getTitle(), 상품_PS5.getPrice(), 상품_PS5.getContent(), List.of(9999L),
			상품_PS5.getCategoryId(), 상품_PS5.getRegionId()
		);
		var response = 상품_수정_요청(유저_만두_액세스_토큰, 1L, itemUpdateRequest);
		return response;
	}
}

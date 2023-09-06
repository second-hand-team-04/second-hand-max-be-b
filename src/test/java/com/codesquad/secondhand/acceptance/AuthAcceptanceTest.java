package com.codesquad.secondhand.acceptance;

import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;
import static com.codesquad.secondhand.util.steps.AuthSteps.로그아웃_요청;
import static com.codesquad.secondhand.util.steps.AuthSteps.로그인_요청;
import static com.codesquad.secondhand.util.steps.AuthSteps.액세스_토큰_발급_요청;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;
import com.codesquad.secondhand.util.AcceptanceTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AuthAcceptanceTest extends AcceptanceTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	/**
	 * Given 유저를 생성하고
	 * When 유저가 로그인하면
	 * Then AccessToken, RefreshToken을 받을 수 있다.
	 */
	@Test
	void 로그인을_한다() {
		// when
		var response = 로그인_요청(유저_만두.getEmail(), 유저_만두.getPassword());

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		토큰이_정상적으로_발급되었는지_검증(response);
	}

	/**
	 * Given 유저를 생성하고
	 * When 해당 유저가 로그인 시 이메일이나 비밀번호가 다르면
	 * Then 요청이 실패된다.
	 */
	@ParameterizedTest
	@MethodSource("providerEmailAndPassword")
	void 로그인_시_이메일이나_비밀번호가_다르면_요청이_실패된다() {
		// when
		var response = 로그인_요청(유저_만두.getEmail(), 유저_만두.getPassword());

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
	}

	/**
	 * Given 유저를 생성하고
	 * And 해당 유저가 로그인하고
	 * When 해당 유저가 로그아웃하면
	 * Then 요청이 성공한다.
	 */
	@Test
	void 로그아웃을_한다() {
		// when
		var response = 로그아웃_요청(유저_만두_액세스_토큰);

		// then
		응답_상태코드_검증(response, HttpStatus.NO_CONTENT);
	}

	/**
	 * Given 유저를 생성하고
	 * And 해당 유저가 로그인하고
	 * When 해당 유저가 로그아웃 시 AccessToken 만료되면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 로그아웃_시_액세스_토큰이_만료되면_요청이_실패된다() {
		// given
		String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaXNzIjoiZmlzaHByaW5jZS5zaXRlIiwiaWF0IjoxNjkzODkxNjcxLCJleHAiOjE2OTM4OTE2NzF9.yLQPEhpnaed8kQ6ZNusP3dmrAML557YYSKSUAFKY6v4";

		// when
		var response = 로그아웃_요청(accessToken);

		// then
		응답_상태코드_검증(response, HttpStatus.FORBIDDEN);
	}

	/**
	 * Given 유저를 생성하고
	 * And 해당 유저가 로그인하고
	 * When AccessToken 발급 요청하면
	 * Then RefreshToken을 받을 수 있다.
	 */
	@Test
	void 액세스_토큰_발급을_한다() {
		// given
		String refreshToken = 로그인_요청(유저_만두.getEmail(), 유저_만두.getPassword()).jsonPath().getString("data.refreshToken");

		// when
		var response = 액세스_토큰_발급_요청(refreshToken);

		// then
		응답_상태코드_검증(response, HttpStatus.OK);
		액세스_토큰이_정상적으로_발급되었는지_검증(response);
	}

	/**
	 * Given 유저를 생성하고
	 * And 해당 유저가 로그인하고
	 * When AccessToken 발급 시 RefreshToken이 만료되거나 DB에 저장 되어있지 않으면
	 * Then 요청이 실패된다.
	 */
	@Test
	void 액세스_토큰_발급_시_리프레쉬_토큰이_만료되거나_DB에_저장_되어있지_않으면_요청이_실패된다() {
		// given
		String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjkzODI4ODE0LCJleHAiOjE2OTY0MjA4MTF9.43qCmfLcT0Nl4r0ODpydlFSec-L_680NNC9gK9FYJ64";

		// when
		var response = 액세스_토큰_발급_요청(refreshToken);

		// then
		응답_상태코드_검증(response, HttpStatus.UNAUTHORIZED);
	}

	private static Stream<Arguments> providerEmailAndPassword() {
		return Stream.of(
			Arguments.of(
				유저_만두.getEmail(),
				"mandu1234!"
			),
			Arguments.of(
				"bono@email.com",
				유저_만두.getPassword()
			)
		);
	}

	private void 토큰이_정상적으로_발급되었는지_검증(ExtractableResponse<Response> response) {
		String accessToken = response.jsonPath().getString("data.accessToken");
		String refreshToken = response.jsonPath().getString("data.refreshToken");

		Assertions.assertAll(
			() -> Assertions.assertDoesNotThrow(() -> jwtTokenProvider.generateAccount(accessToken)),
			() -> Assertions.assertDoesNotThrow(() -> jwtTokenProvider.generateAccount(refreshToken))
		);
	}

	private void 액세스_토큰이_정상적으로_발급되었는지_검증(ExtractableResponse<Response> response) {
		String accessToken = response.jsonPath().getString("data.accessToken");

		Assertions.assertDoesNotThrow(() -> jwtTokenProvider.generateAccount(accessToken));
	}
}

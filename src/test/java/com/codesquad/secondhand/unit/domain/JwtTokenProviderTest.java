package com.codesquad.secondhand.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;

public class JwtTokenProviderTest {

	private static String SECRET_KEY = "test1test2test3test4test5test6test7";

	private JwtTokenProvider jwtTokenProvider;
	private Map<String, Object> claims;

	@BeforeEach
	void init() {
		claims = Map.of("id", 1L);
		jwtTokenProvider = new JwtTokenProvider(SECRET_KEY, 1800000L, 1209600000L, "mandu.com");
	}

	@Test
	void 액세스_토큰을_발급한다() {
		// when
		String actual = jwtTokenProvider.generateAccessToken(claims);

		// then
		Assertions.assertAll(
			() -> assertThat(actual).isNotBlank(),
			() -> assertThat(jwtTokenProvider.isExpired(actual)).isFalse(),
			() -> assertThat(jwtTokenProvider.generateAccount(actual).getId()).isEqualTo(1L)
		);
	}

	@Test
	void 리프레쉬_토큰을_발급한다() {
		// when
		String actual = jwtTokenProvider.generateRefreshToken(claims);

		// then
		Assertions.assertAll(
			() -> assertThat(actual).isNotBlank(),
			() -> assertThat(jwtTokenProvider.isExpired(actual)).isFalse()
		);
	}

	@Test
	void 토큰으로_계정을_반환한다() {
		// given
		String accessToken = jwtTokenProvider.generateAccessToken(claims);

		// when
		Account actual = jwtTokenProvider.generateAccount(accessToken);

		// then
		assertThat(actual.getId()).isEqualTo(actual.getId());
	}

	@Test
	void 토큰으로_계정_반환_시_검증이_실패되면_예외가_발생한다() {
		// given
		String secretKey = "test1secretKeytest1secretKeytest1secretKey";
		JwtTokenProvider jwtTokenProvider2 = new JwtTokenProvider(secretKey, 0L, 0L, "mandu.com");
		String accessToken = jwtTokenProvider.generateAccessToken(claims);
		String accessToken2 = jwtTokenProvider2.generateAccessToken(claims);

		// then
		Assertions.assertAll(
			() -> Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider2.generateAccount(accessToken)),
			() -> Assertions.assertThrows(RuntimeException.class, () -> jwtTokenProvider2.generateAccount(accessToken2))
		);

	}

	@ParameterizedTest
	@MethodSource("providerJwtTokenProviderAndExpected")
	void 만료된_토큰을_확인한다(JwtTokenProvider jwtTokenProvider, boolean expected) {
		// given
		String accessToken = jwtTokenProvider.generateAccessToken(claims);

		// when
		boolean actual = jwtTokenProvider.isExpired(accessToken);

		// then
		assertThat(actual).isEqualTo(expected);
	}

	private static Stream<Arguments> providerJwtTokenProviderAndExpected() {
		return Stream.of(
			Arguments.of(
				new JwtTokenProvider(SECRET_KEY, 0L, 0L, "mandu.com")
				, true
			),
			Arguments.of(
				new JwtTokenProvider(SECRET_KEY, 1800000L, 1209600000L, "mandu.com")
				, false
			)
		);
	}
}

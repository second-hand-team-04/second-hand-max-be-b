package com.codesquad.secondhand.unit.domain;

import static com.codesquad.secondhand.util.fixture.ProviderFixture.공급자_내부;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_신교동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_청운동;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;

public class UserTest {

	private User user;
	private Region region;
	private Region region2;

	@BeforeEach
	void init() {
		user = new User(1L, 공급자_내부.toProvider(), new Image(1L, "http://www.image.com/만두.jpg"),
			유저_만두.getNickname(), 유저_만두.getEmail(), 유저_만두.getPassword(), LocalDateTime.now());
		region = 동네_서울_종로구_청운동.getRegion();
		region2 = 동네_서울_종로구_신교동.getRegion();
	}

	@Test
	void 나의_동네를_등록한다() {
		// when
		user.addMyRegion(region);

		// then
		assertThat(user.getRegions()).containsExactly(region);
	}

	@Test
	void 나의_동네를_삭제한다() {
		// given
		user.addMyRegion(region);
		user.addMyRegion(region2);

		// when
		user.removeMyRegion(region.getId());

		// then
		assertThat(user.getRegions()).doesNotContain(region);
	}

	@ParameterizedTest
	@MethodSource("providerNicknameAndImage")
	void 프로필을_수정한다(String nickname, boolean isImageChanged, Image image, String expectedImageUrl) {
		// when
		user.updateProfile(nickname, isImageChanged, image);

		// then
		Assertions.assertAll(
			() -> assertThat(user.getNickname()).isEqualTo(nickname),
			() -> assertThat(user.getImageUrl()).isEqualTo(expectedImageUrl)
		);
	}

	private static Stream<Arguments> providerNicknameAndImage() {
		return Stream.of(
			Arguments.of(
				"갈비만두",
				true,
				new Image(2L, "http://www.image.com/갈비만두.jpg"),
				"http://www.image.com/갈비만두.jpg"
			),
			Arguments.of(
				유저_만두.getNickname(),
				true,
				new Image(2L, "http://www.image.com/갈비만두.jpg"),
				"http://www.image.com/갈비만두.jpg"
			),

			Arguments.of(
				유저_만두.getNickname(),
				false,
				new Image(2L, "http://www.image.com/갈비만두.jpg"),
				"http://www.image.com/만두.jpg"
			),
			Arguments.of(
				"갈비만두",
				true,
				null,
				null
			),
			Arguments.of(
				유저_만두.getNickname(),
				false,
				null,
				"http://www.image.com/만두.jpg"
			)
		);
	}
}

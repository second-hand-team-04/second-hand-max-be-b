package com.codesquad.secondhand.unit.domain;

import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_신교동;
import static com.codesquad.secondhand.util.fixture.RegionFixture.동네_서울_종로구_청운동;
import static com.codesquad.secondhand.util.fixture.UserFixture.유저_만두;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.User;

public class UserTest {

	private User user;
	private Region region;
	private Region region2;

	@BeforeEach
	void init() {
		user = 유저_만두.getUser();
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
}

package com.codesquad.secondhand.unit.domain;

import static com.codesquad.secondhand.util.fixture.ProviderFixture.*;
import static com.codesquad.secondhand.util.fixture.RegionFixture.*;
import static com.codesquad.secondhand.util.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.codesquad.secondhand.common.exception.userregion.UserRegionDuplicationException;
import com.codesquad.secondhand.common.exception.userregion.UserRegionMaxAddCountException;
import com.codesquad.secondhand.common.exception.userregion.UserRegionMinRemoveCountException;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.MyRegions;
import com.codesquad.secondhand.user.domain.User;
import com.codesquad.secondhand.user.domain.UserRegion;

public class MyRegionsTest {

	private MyRegions myRegions;
	private User user;
	private Region region;
	private Region region2;
	private UserRegion userRegion;
	private UserRegion userRegion2;

	@BeforeEach
	void init() {
		myRegions = new MyRegions();
		user = new User(1L, 공급자_내부.toProvider(), null, 동네_서울_강남구_역삼동.getRegion(),
			유저_만두.getNickname(), 유저_만두.getEmail(), 유저_만두.getPassword(), LocalDateTime.now());
		region = 동네_서울_종로구_청운동.getRegion();
		region2 = 동네_서울_종로구_신교동.getRegion();
		userRegion = new UserRegion(user, region);
		userRegion2 = new UserRegion(user, region2);
	}

	@Test
	void 나의_동네를_등록한다() {
		// when
		myRegions.addUserRegion(userRegion);

		// then
		assertThat(myRegions.getRegions()).containsExactly(region);
	}

	@Test
	void 나의_동네_등록_시_이미_등록된_동네이면_에러를_반환한다() {
		// given
		myRegions.addUserRegion(userRegion);

		// then
		Assertions.assertThrows(UserRegionDuplicationException.class,
			() -> myRegions.addUserRegion(userRegion));
	}

	@Test
	void 나의_동네_등록_시_이미_등록된_동네가_2개이면_에러를_반환한다() {
		// given
		myRegions.addUserRegion(userRegion);
		myRegions.addUserRegion(userRegion2);

		// then
		Assertions.assertThrows(UserRegionMaxAddCountException.class,
			() -> myRegions.addUserRegion(userRegion));
	}

	@Test
	void 나의_동네를_삭제한다() {
		// given
		myRegions.addUserRegion(userRegion);
		myRegions.addUserRegion(userRegion2);

		// when
		myRegions.removeUserRegion(region);

		// then
		assertThat(myRegions.getRegions()).doesNotContain(region);
	}

	@Test
	void 나의_동네_삭제_시_나의_동네가_1개인_경우_에러를_반환한다() {
		// given
		myRegions.addUserRegion(userRegion);

		// then
		Assertions.assertThrows(UserRegionMinRemoveCountException.class,
			() -> myRegions.removeUserRegion(region));
	}
}

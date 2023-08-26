package com.codesquad.secondhand.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.common.exception.userregion.UserRegionDuplicationException;
import com.codesquad.secondhand.common.exception.userregion.UserRegionMaxAddCountException;
import com.codesquad.secondhand.common.exception.userregion.UserRegionMinRemoveCountException;
import com.codesquad.secondhand.region.domain.Region;

@Embeddable
public class MyRegions {

	private static final int MAX_ADD_REGION_COUNT = 2;
	private static final int MIN_REMOVE_COUNT = 1;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRegion> userRegions;

	public MyRegions() {
		userRegions = new ArrayList<>();
	}

	public List<Region> getRegions() {
		return userRegions.stream()
			.map(UserRegion::getRegion)
			.collect(Collectors.toUnmodifiableList());
	}

	public void addUserRegion(UserRegion userRegion) {
		validateMaxAddRegionCount();
		validateDuplicationRegion(userRegion.getRegion());
		userRegions.add(userRegion);
	}

	private void validateDuplicationRegion(Region region) {
		if (userRegions.stream().anyMatch(u -> u.equalsRegion(region))) {
			throw new UserRegionDuplicationException();
		}
	}

	private void validateMaxAddRegionCount() {
		if (userRegions.size() >= MAX_ADD_REGION_COUNT) {
			throw new UserRegionMaxAddCountException();
		}
	}

	public void removeUserRegion(Long regionId) {
		validateMinRemoveCount();
		userRegions.remove(findByRegionId(regionId));
	}

	private void validateMinRemoveCount() {
		if (userRegions.size() <= MIN_REMOVE_COUNT) {
			throw new UserRegionMinRemoveCountException();
		}
	}

	private UserRegion findByRegionId(Long regionId) {
		return userRegions.stream()
			.filter(u -> u.equalsRegion(regionId))
			.findAny()
			.orElse(null);
	}
}

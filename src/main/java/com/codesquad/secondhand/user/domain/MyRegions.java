package com.codesquad.secondhand.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import com.codesquad.secondhand.region.domain.Region;

import lombok.AllArgsConstructor;

@Embeddable
public class MyRegions {

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
}

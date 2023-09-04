package com.codesquad.secondhand.user.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.codesquad.secondhand.region.domain.Region;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRegion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	private Region region;

	public UserRegion(User user, Region region) {
		this.user = user;
		this.region = region;
	}

	public boolean equalsRegion(Region region) {
		return this.region.equals(region);
	}

	public boolean equalsRegion(Long regionId) {
		return region.equalsId(regionId);
	}
}

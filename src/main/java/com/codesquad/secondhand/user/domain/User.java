package com.codesquad.secondhand.user.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.codesquad.secondhand.region.domain.Region;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String loginId;

	private String nickname;

	private String email;

	private String password;

	private String profile;

	private LocalDateTime createdAt;

	@Embedded
	private MyRegions myRegions;

	public User() {
		myRegions = new MyRegions();
	}

	public List<Region> getRegions() {
		return myRegions.getRegions();
	}

	public void addMyRegion(Region region) {
		myRegions.addUserRegion(new UserRegion(this, region));
	}

	public Long getId() {
		return id;
	}

	public String getLoginId() {
		return loginId;
	}

	public String getNickname() {
		return nickname;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getProfile() {
		return profile;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}

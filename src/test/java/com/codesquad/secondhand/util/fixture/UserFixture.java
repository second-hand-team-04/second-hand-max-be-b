package com.codesquad.secondhand.util.fixture;

import static com.codesquad.secondhand.util.fixture.ProviderFixture.공급자_내부;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import com.codesquad.secondhand.category.application.dto.CategoryItemDetailResponse;
import com.codesquad.secondhand.user.application.dto.UserItemDetailResponse;
import com.codesquad.secondhand.user.domain.User;

public enum UserFixture {

	유저_만두(1L, 공급자_내부.getId(), null, "만두", "mandu@mandu.com", "test1234!", LocalDateTime.now()),
	유저_보노(2L, 공급자_내부.getId(), null, "보노", "bono@bono.com", "test1234!", LocalDateTime.now()),
	유저_지구(3L, 공급자_내부.getId(), null, "지구", "earth@earth.com", "test1234!", LocalDateTime.now()),
	유저_피아(4L, 공급자_내부.getId(), null, "피아", "fia@fia.com", "test1234!", LocalDateTime.now());

	private final Long id;
	private final Long providerId;
	private final Long imageId;
	private final String nickname;
	private final String email;
	private final String password;
	private final LocalDateTime createdAt;

	UserFixture(Long id, Long providerId, Long imageId, String nickname, String email, String password,
		LocalDateTime createdAt) {
		this.id = id;
		this.providerId = providerId;
		this.imageId = imageId;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
	}

	public static UserFixture findById(Long id) {
		return Arrays.stream(values())
			.filter(u -> Objects.equals((u.getId()), id))
			.findAny()
			.orElseThrow();
	}

	public static UserItemDetailResponse findUserItemDetailResponseById(Long id) {
		return findById(id).toUserItemDetailResponse();
	}

	public UserItemDetailResponse toUserItemDetailResponse() {
		return new UserItemDetailResponse(id, nickname);
	}

	public Long getId() {
		return id;
	}

	public Long getProviderId() {
		return providerId;
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

	public Long getImageId() {
		return imageId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}

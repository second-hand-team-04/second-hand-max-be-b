package com.codesquad.secondhand.util.fixture;

import static com.codesquad.secondhand.util.fixture.ImageFixture.이미지_기본_사용자_프로필;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.codesquad.secondhand.user.domain.User;

public enum UserFixture {

	유저_만두(1L, 1L, 이미지_기본_사용자_프로필.getId(), "만두", "mandu@mandu.com", "test", LocalDateTime.now()),
	유저_보노(2L, 1L, 이미지_기본_사용자_프로필.getId(), "보노", "bono@bono.com", "test", LocalDateTime.now());

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

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO `user`(provider_id, image_id, email, nickname, password, created_at) VALUES %s",
			Arrays.stream(values())
				.map(u -> String.format(
					"(%s, %s, '%s', '%s', '%s', '%s')",
					u.getProviderId(),
					u.getImageId(),
					u.getEmail(),
					u.getNickname(),
					u.getPassword(),
					u.getCreatedAt()))
				.collect(Collectors.joining(", ")));
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

	public User getUser() {
		return new User(id, ProviderFixture.findById(providerId).toProvider(), ImageFixture.findById(imageId).toImage(),
			nickname, email, password, createdAt);
	}
}

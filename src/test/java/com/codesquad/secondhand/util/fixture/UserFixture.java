package com.codesquad.secondhand.util.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.codesquad.secondhand.user.domain.User;

public enum UserFixture {

	유저_만두(1L, "만두", "mandu@mandu.com", "test", "http://image.com/mandu.jpg", LocalDateTime.now()),
	유저_보노(2L, "보노", "bono@bono.com", "test", "http://image.com/bono.jpg", LocalDateTime.now());

	private final Long id;
	private final String nickname;
	private final String email;
	private final String password;
	private final String profile;
	private final LocalDateTime createdAt;

	UserFixture(Long id, String nickname, String email, String password, String profile,
		LocalDateTime createdAt) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profile = profile;
		this.createdAt = createdAt;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO `user`(nickname, email, password, profile, created_at) VALUES %s",
			Arrays.stream(values())
				.map(u -> String.format(
					"('%s', '%s', '%s', '%s', '%s')",
					u.getNickname(),
					u.getEmail(),
					u.getPassword(),
					u.getProfile(),
					u.getCreatedAt()))
				.collect(Collectors.joining(", ")));
	}

	public Long getId() {
		return id;
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

	public User getUser() {
		return new User(id, nickname, email, password, profile, createdAt);
	}
}

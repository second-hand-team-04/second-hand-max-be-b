package com.codesquad.secondhand.util.fixture;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.codesquad.secondhand.user.domain.User;

public enum UserFixture {

	유저_만두(1L, "mandu", "만두", "mandu@mandu.com", "test", "http://image.com/mandu.jpg", LocalDateTime.of(2023, Month.APRIL, 20, 10, 14, 10));

	private final Long id;
	private final String loginId;
	private final String nickname;
	private final String email;
	private final String password;
	private final String profile;
	private final LocalDateTime createdAt;

	UserFixture(Long id, String loginId, String nickname, String email, String password, String profile,
		LocalDateTime createdAt) {
		this.id = id;
		this.loginId = loginId;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.profile = profile;
		this.createdAt = createdAt;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO `user`(login_id, nickname, email, password, profile, created_at) VALUES %s",
			Arrays.stream(values())
				.map(u -> String.format(
					"('%s', '%s', '%s', '%s', '%s', '%s')",
					u.getLoginId(),
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

	public User getUser() {
		return new User(id, loginId, nickname, email, password, profile, createdAt);
	}
}

package com.codesquad.secondhand.auth.domain;

import com.codesquad.secondhand.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Account {

	private Long id;
	private String email;
	private String nickname;

	public static Account from(User user) {
		return new Account(user.getId(), user.getEmail(), user.getNickname());
	}
}

package com.codesquad.secondhand.user.application.dto;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserCreateRequest {

	private Long providerId;
	private String email;
	private String nickname;
	private String password;

	public User toUser(Image image) {
		return new User(nickname, email, password, image);
	}
}

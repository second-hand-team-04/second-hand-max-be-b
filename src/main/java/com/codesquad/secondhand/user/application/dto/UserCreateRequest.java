package com.codesquad.secondhand.user.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.codesquad.secondhand.Image.domain.Image;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateRequest {

	private Long providerId;

	@Email
	private String email;

	@Pattern(regexp = "^(?=.*[a-zA-Z가-힣])[a-zA-Z가-힣0-9]{2,10}$")
	private String nickname;

	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$")
	private String password;

	public UserCreateRequest(Long providerId, String email, String nickname, String password) {
		this.providerId = providerId;
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public User toUser(Provider provider, Image image) {
		return new User(provider, nickname, email, password, image);
	}

	public void injectProviderId(Long providerId) {
		this.providerId = providerId;
	}
}

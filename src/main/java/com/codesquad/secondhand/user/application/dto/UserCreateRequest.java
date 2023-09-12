package com.codesquad.secondhand.user.application.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.codesquad.secondhand.image.domain.Image;
import com.codesquad.secondhand.region.domain.Region;
import com.codesquad.secondhand.user.domain.Provider;
import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCreateRequest {

	private Long providerId;

	@Email(message = "이메일 형식이 올바르지 않습니다")
	private String email;

	@Pattern(regexp = "^(?=.*[a-zA-Z가-힣])[a-zA-Z가-힣0-9]{2,10}$", message = "닉네임은 2자 이상 10자 이하, 영문 또는 한글을 포함해야 합니다")
	private String nickname;

	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$", message = "비밀번호는 8자 이상 16자 이하로, 영문, 숫자, 특수문자를 최소 1개씩 포함해야 합니다")
	private String password;

	public UserCreateRequest(Long providerId, String email, String nickname, String password) {
		this.providerId = providerId;
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}

	public User toUser(Provider provider, Image image, Region region) {
		return new User(provider, nickname, email, password, image, region);
	}

	public void injectProviderId(Long providerId) {
		this.providerId = providerId;
	}
}

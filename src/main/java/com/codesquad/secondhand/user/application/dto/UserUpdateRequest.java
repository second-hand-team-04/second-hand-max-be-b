package com.codesquad.secondhand.user.application.dto;

import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserUpdateRequest {

	private Long id;

	@Pattern(regexp = "^(?=.*[a-zA-Z가-힣])[a-zA-Z가-힣0-9]{2,10}$", message = "닉네임은 2자 이상 10자 이하, 영문 또는 한글을 포함해야 합니다")
	private String nickname;

	public UserUpdateRequest(String nickname) {
		this.nickname = nickname;
	}

	public void injectId(Long id) {
		this.id = id;
	}
}

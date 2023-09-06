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

	private boolean isImageChanged;

	public UserUpdateRequest(String nickname, boolean isImageChanged) {
		this.nickname = nickname;
		this.isImageChanged = isImageChanged;
	}

	public boolean getIsImageChanged() {
		return isImageChanged;
	}

	public void injectId(Long id) {
		this.id = id;
	}
}

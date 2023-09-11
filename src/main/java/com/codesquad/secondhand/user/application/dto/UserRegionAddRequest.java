package com.codesquad.secondhand.user.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRegionAddRequest {

	private Long userId;
	private Long id;

	public UserRegionAddRequest(Long id) {
		this.id = id;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
	}
}

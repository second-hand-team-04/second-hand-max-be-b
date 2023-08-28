package com.codesquad.secondhand.user.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserRegionAddRequest {

	private Long userId;
	private Long regionId;

	public UserRegionAddRequest(Long regionId) {
		this.regionId = regionId;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
	}
}

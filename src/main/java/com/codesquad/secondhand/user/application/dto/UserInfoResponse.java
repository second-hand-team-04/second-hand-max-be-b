package com.codesquad.secondhand.user.application.dto;

import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserInfoResponse {

	private Long userId;

	private String nickname;

	private String imageUrl;

	public static UserInfoResponse from(User user) {
		return new UserInfoResponse(
			user.getId(),
			user.getNickname(),
			user.getImageUrl());
	}
}

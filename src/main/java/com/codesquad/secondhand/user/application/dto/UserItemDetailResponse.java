package com.codesquad.secondhand.user.application.dto;

import com.codesquad.secondhand.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserItemDetailResponse {

	private Long id;
	private String nickname;

	public static UserItemDetailResponse from(User user) {
		return new UserItemDetailResponse(user.getId(), user.getNickname());
	}
}

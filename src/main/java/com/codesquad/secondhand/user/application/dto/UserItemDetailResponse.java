package com.codesquad.secondhand.user.application.dto;

import java.io.Serializable;

import com.codesquad.secondhand.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserItemDetailResponse implements Serializable {

	private Long id;
	private String nickname;

	public static UserItemDetailResponse from(User user) {
		return new UserItemDetailResponse(user.getId(), user.getNickname());
	}
}

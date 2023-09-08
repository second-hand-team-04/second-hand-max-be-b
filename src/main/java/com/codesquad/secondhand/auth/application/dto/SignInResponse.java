package com.codesquad.secondhand.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInResponse {

	private String accessToken;
	private String refreshToken;
}

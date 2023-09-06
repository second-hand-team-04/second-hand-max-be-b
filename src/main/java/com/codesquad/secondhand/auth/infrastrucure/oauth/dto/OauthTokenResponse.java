package com.codesquad.secondhand.auth.infrastrucure.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OauthTokenResponse {

	@JsonProperty("access_token")
	private String accessToken;

	private String scope;

	@JsonProperty("token_type")
	private String tokenType;
}

package com.codesquad.secondhand.auth.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OauthUserInfomation {

	private String email;
	private String nickname;

	public static OauthUserInfomation of(Map<String, Object> result, ProviderType providerType) {
		return OauthAttributes.getOauthUserInfomation(result, providerType);
	}
}

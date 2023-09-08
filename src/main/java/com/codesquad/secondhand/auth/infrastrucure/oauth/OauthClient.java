package com.codesquad.secondhand.auth.infrastrucure.oauth;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.codesquad.secondhand.auth.domain.ProviderType;
import com.codesquad.secondhand.auth.infrastrucure.oauth.OauthProperties.OauthProperty;
import com.codesquad.secondhand.auth.infrastrucure.oauth.dto.OauthTokenResponse;
import com.codesquad.secondhand.auth.domain.OauthUserInfomation;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OauthClient {

	private final OauthProperties oauthProperties;

	public OauthTokenResponse generateAccessToken(ProviderType providerType, String code) {
		OauthProperty oauthProperty = oauthProperties.findByProviderName(providerType);
		return WebClient.create()
			.post()
			.uri(oauthProperty.getTokenUrl())
			.headers(header -> {
				header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
			})
			.bodyValue(getTokenRequest(oauthProperty, code))
			.retrieve()
			.bodyToMono(OauthTokenResponse.class)
			.block();
	}

	private MultiValueMap<String, String> getTokenRequest(OauthProperty oauthProperty, String code) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("code", code);
		formData.add("grant_type", "authorization_code");
		formData.add("redirect_uri", oauthProperty.getRedirectUrl());
		formData.add("client_id", oauthProperty.getClientId());
		formData.add("client_secret", oauthProperty.getClientSecret());
		return formData;
	}

	public OauthUserInfomation generateUserInfomation(ProviderType providerType, String accessToken) {
		OauthProperty oauthProperty = oauthProperties.findByProviderName(providerType);
		Map<String, Object> result = WebClient.create()
			.get()
			.uri(oauthProperty.getUserInfoUrl())
			.headers(header -> {
				header.setBearerAuth(accessToken);
				header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			})
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
			.block();
		return OauthUserInfomation.of(result, providerType);
	}
}

package com.codesquad.secondhand.auth.infrastrucure.oauth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.auth.domain.ProviderType;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "oauth2")
@Getter
public class OauthProperties {

	private final Map<String, OauthProperty> oauthProperties = new HashMap<>();

	@Getter
	@Setter
	public static class OauthProperty {

		private String clientId;
		private String clientSecret;
		private String redirectUrl;
		private String tokenUrl;
		private String userInfoUrl;
	}

	public OauthProperty findByProviderName(ProviderType providerType) {
		return oauthProperties.get(providerType.getLowerCaseName());
	}
}

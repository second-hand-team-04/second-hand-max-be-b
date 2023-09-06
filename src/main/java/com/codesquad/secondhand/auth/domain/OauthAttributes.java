package com.codesquad.secondhand.auth.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public enum OauthAttributes {

	KAKAO(ProviderType.KAKAO, Mapper.kakao);

	private final ProviderType providerType;
	private final Function<Map<String, Object>, OauthUserInfomation> function;

	OauthAttributes(ProviderType providerType, Function<Map<String, Object>, OauthUserInfomation> function) {
		this.providerType = providerType;
		this.function = function;
	}

	public static OauthUserInfomation getOauthUserInfomation(Map<String, Object> result, ProviderType providerType) {
		return Arrays.stream(values())
			.filter(o -> o.providerType == providerType)
			.findAny()
			.map(o -> o.function.apply(result))
			.orElseThrow();
	}

	protected static class Mapper {

		private static final String SITE = "fishprince.site";

		protected static Function<Map<String, Object>, OauthUserInfomation> kakao =
			attribute -> new OauthUserInfomation(String.format("%s@%s", attribute.get("id"), SITE), getUUIDNickname());

		private static String getUUIDNickname() {
			String uuid = UUID.randomUUID().toString();
			return uuid.replaceAll("-", "").substring(0, 10);
		}
	}
}

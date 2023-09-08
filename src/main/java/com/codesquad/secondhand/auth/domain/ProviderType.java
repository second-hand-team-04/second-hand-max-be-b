package com.codesquad.secondhand.auth.domain;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum ProviderType {

	LOCAL(1L),
	KAKAO(2L);

	private final Long id;

	ProviderType(Long id) {
		this.id = id;
	}

	public static ProviderType findByName(String providerName) {
		return Arrays.stream(values())
			.filter(p -> p.name().equalsIgnoreCase(providerName))
			.findAny()
			.orElseThrow();
	}

	public String getLowerCaseName() {
		return name().toLowerCase();
	}
}

package com.codesquad.secondhand.auth.domain;

import lombok.Getter;

@Getter
public enum ProviderType {

	LOCAL(1L),
	KAKAO(2L);

	private final Long id;

	ProviderType(Long id) {
		this.id = id;
	}
}

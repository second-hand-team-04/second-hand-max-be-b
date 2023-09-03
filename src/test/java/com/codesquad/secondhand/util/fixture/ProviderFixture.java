package com.codesquad.secondhand.util.fixture;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import com.codesquad.secondhand.user.domain.Provider;

public enum ProviderFixture {

	공급자_내부(1L, "LOCAL"),
	공급자_카카오(2L, "KAKAO");

	private final Long id;
	private final String type;

	ProviderFixture(Long id, String type) {
		this.id = id;
		this.type = type;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO `provider`(type) VALUES %s",
			Arrays.stream(values())
				.map(p -> String.format(
					"('%s')",
					p.getType()))
				.collect(Collectors.joining(", ")));
	}

	public static ProviderFixture findById(Long id) {
		return Arrays.stream(values())
			.filter(p -> Objects.equals((p.getId()), id))
			.findAny()
			.orElseThrow();
	}

	public Provider toProvider() {
		return new Provider(id, type);
	}

	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}
}

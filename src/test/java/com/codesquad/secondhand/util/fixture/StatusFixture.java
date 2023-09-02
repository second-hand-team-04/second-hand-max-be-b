package com.codesquad.secondhand.util.fixture;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public enum StatusFixture {

	판매중(1L, "판매중"),
	예약중(2L, "예약중"),
	거래완료(3L, "거래완료");

	private final Long id;
	private final String type;

	StatusFixture(Long id, String type) {
		this.id = id;
		this.type = type;
	}

	public static String createInsertSQL() {
		return String.format(
			"INSERT INTO `status`(type) VALUES %s",
			Arrays.stream(values())
				.map(status -> String.format(
					"('%s')",
					status.getType()
				))
				.collect(Collectors.joining(", "))
		);
	}

	public static StatusFixture findById(Long id) {
		return Arrays.stream(values())
			.filter(status -> Objects.equals(status.getId(), id))
			.findAny()
			.orElseThrow();
	}

	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}
}

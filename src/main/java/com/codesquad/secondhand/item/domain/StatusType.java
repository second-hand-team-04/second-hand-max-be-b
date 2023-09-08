package com.codesquad.secondhand.item.domain;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public enum StatusType {

	FOR_SALE(1L),
	SOLD(2L),
	RESERVED(3L);

	private final Long id;

	StatusType(Long id) {
		this.id = id;
	}

	public static boolean existsById(Long id) {
		return Arrays.stream(values())
			.map(StatusType::getId)
			.collect(Collectors.toList())
			.contains(id);
	}
}

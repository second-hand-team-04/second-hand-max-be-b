package com.codesquad.secondhand.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Account {

	private Long id;

	@Override
	public String toString() {
		return "Account{" +
			"id=" + id +
			'}';
	}
}

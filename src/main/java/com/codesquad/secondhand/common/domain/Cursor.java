package com.codesquad.secondhand.common.domain;

import com.codesquad.secondhand.common.exception.cursor.CursorNotPositiveNumberException;
import com.codesquad.secondhand.common.exception.cursor.CursorNumberFormatException;

import lombok.Getter;

@Getter
public class Cursor {

	private final int value;

	public Cursor(String value) {
		validatePositiveNumber(value);
		this.value = Integer.parseInt(value);
	}

	private void validatePositiveNumber(String value) {
		try {
			int number = Integer.parseInt(value);

			if (number < 0) {
				throw new CursorNotPositiveNumberException();
			}
		} catch (NumberFormatException e) {
			throw new CursorNumberFormatException();
		}
	}
}

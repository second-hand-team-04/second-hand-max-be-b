package com.codesquad.secondhand.unit.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.codesquad.secondhand.common.domain.Cursor;
import com.codesquad.secondhand.common.exception.cursor.CursorNotPositiveNumberException;
import com.codesquad.secondhand.common.exception.cursor.CursorNumberFormatException;

public class CursorTest {

	@Test
	public void 커서를_생성한다() {
		// when
		Cursor cursor = new Cursor("1");

		// then
		assertThat(cursor.getValue()).isEqualTo(1);
	}

	@Test
	public void 커서_생성_시_숫자가_아닌_경우_예외가_발생한다() {
		// then
		Assertions.assertThrows(CursorNumberFormatException.class, () -> new Cursor("존재하지 않는 숫자"));
	}

	@Test
	public void 커서_생성_시_양수가_아닌_경우_예외가_발생한다() {
		// then
		Assertions.assertThrows(CursorNotPositiveNumberException.class, () -> new Cursor("-1"));
	}
}

package com.codesquad.secondhand.common.exception.cursor;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class CursorNotPositiveNumberException extends CustomException {

	public CursorNotPositiveNumberException() {
		super(ErrorType.CURSOR_NOT_POSITIVE);
	}
}

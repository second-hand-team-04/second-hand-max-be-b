package com.codesquad.secondhand.common.exception.cursor;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class CursorNumberFormatException extends CustomException {

	public CursorNumberFormatException() {
		super(ErrorType.NUMBER_FORMAT);
	}
}

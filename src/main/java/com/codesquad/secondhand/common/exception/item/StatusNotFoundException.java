package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class StatusNotFoundException extends CustomException {
	public StatusNotFoundException() {
		super(ErrorType.STATUS_NOT_FOUND);
	}
}

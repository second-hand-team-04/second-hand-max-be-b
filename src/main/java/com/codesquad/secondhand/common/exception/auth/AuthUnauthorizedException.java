package com.codesquad.secondhand.common.exception.auth;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class AuthUnauthorizedException extends CustomException {

	public AuthUnauthorizedException(ErrorType errorType) {
		super(errorType);
	}
}

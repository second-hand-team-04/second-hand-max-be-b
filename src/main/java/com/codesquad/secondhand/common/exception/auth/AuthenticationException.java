package com.codesquad.secondhand.common.exception.auth;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class AuthenticationException extends CustomException {

	public AuthenticationException() {
		super(ErrorType.AUTHENTICATION);
	}
}

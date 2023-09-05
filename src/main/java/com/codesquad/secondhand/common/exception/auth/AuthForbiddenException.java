package com.codesquad.secondhand.common.exception.auth;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class AuthForbiddenException extends CustomException {

	public AuthForbiddenException() {
		super(ErrorType.AUTH_ACCESS_TOKEN_FORBIDDEN);
	}
}

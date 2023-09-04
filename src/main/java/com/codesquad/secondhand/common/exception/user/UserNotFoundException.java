package com.codesquad.secondhand.common.exception.user;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserNotFoundException extends CustomException {

	public UserNotFoundException() {
		super(ErrorType.USER_NOT_FOUND);
	}
}

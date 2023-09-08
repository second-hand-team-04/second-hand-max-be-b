package com.codesquad.secondhand.common.exception.user;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserLoginInfoDifferentException extends CustomException {

	public UserLoginInfoDifferentException() {
		super(ErrorType.USER_LOGIN_INFO_DIFFERENT);
	}
}

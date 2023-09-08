package com.codesquad.secondhand.common.exception.user;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserNicknameDuplicationException extends CustomException {

	public UserNicknameDuplicationException() {
		super(ErrorType.USER_NICKNAME_DUPLICATION);
	}
}

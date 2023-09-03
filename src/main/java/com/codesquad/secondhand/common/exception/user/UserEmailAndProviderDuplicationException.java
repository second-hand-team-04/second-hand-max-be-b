package com.codesquad.secondhand.common.exception.user;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserEmailAndProviderDuplicationException extends CustomException {

	public UserEmailAndProviderDuplicationException() {
		super(ErrorType.USER_EMAIL_PROVIDER_DUPLICATION);
	}
}

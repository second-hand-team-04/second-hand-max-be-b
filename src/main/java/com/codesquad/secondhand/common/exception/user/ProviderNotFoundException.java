package com.codesquad.secondhand.common.exception.user;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ProviderNotFoundException extends CustomException {

	public ProviderNotFoundException() {
		super(ErrorType.PROVIDER_NOT_FOUND);
	}
}

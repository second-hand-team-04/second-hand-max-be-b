package com.codesquad.secondhand.common.exception.userregion;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserRegionMaxAddCountException extends CustomException {

	public UserRegionMaxAddCountException() {
		super(ErrorType.USER_REGION_MAX_ADD_COUNT);
	}
}

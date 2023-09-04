package com.codesquad.secondhand.common.exception.userregion;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserRegionDuplicationException extends CustomException {

	public UserRegionDuplicationException() {
		super(ErrorType.USER_REGION_DUPLICATION);
	}
}

package com.codesquad.secondhand.common.exception.userregion;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class UserRegionMinRemoveCountException extends CustomException {

	public UserRegionMinRemoveCountException() {
		super(ErrorType.USER_REGION_MIN_REMOVE_COUNT);
	}
}

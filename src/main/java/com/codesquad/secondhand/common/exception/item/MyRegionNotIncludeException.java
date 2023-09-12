package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class MyRegionNotIncludeException extends CustomException {

	public MyRegionNotIncludeException() {
		super(ErrorType.USER_REGION_NOT_INCLUDE);
	}
}

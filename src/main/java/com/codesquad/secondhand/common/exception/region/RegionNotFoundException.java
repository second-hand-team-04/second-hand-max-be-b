package com.codesquad.secondhand.common.exception.region;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class RegionNotFoundException extends CustomException {

	public RegionNotFoundException() {
		super(ErrorType.REGION_NOT_FOUND);
	}
}

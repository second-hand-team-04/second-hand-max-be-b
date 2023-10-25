package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class PermissionException extends CustomException {

	public PermissionException() {
		super(ErrorType.USER_PERMISSION);
	}
}

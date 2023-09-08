package com.codesquad.secondhand.common.exception.image;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class MaxUploadSizeException extends CustomException {

	public MaxUploadSizeException() {
		super(ErrorType.MAX_UPLOAD_SIZE);
	}
}

package com.codesquad.secondhand.common.exception.image;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ImageInvalidTypeException extends CustomException {

	public ImageInvalidTypeException() {
		super(ErrorType.IMAGE_INVALID_TYPE);
	}
}

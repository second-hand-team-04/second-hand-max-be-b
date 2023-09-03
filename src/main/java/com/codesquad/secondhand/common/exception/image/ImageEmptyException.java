package com.codesquad.secondhand.common.exception.image;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ImageEmptyException extends CustomException {

	public ImageEmptyException() {
		super(ErrorType.IMAGE_EMPTY);
	}
}

package com.codesquad.secondhand.common.exception.image;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ImageNotFoundException extends CustomException {

	public ImageNotFoundException() {
		super(ErrorType.IMAGE_NOT_FOUND);
	}
}

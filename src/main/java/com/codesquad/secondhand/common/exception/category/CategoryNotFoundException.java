package com.codesquad.secondhand.common.exception.category;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class CategoryNotFoundException extends CustomException {
	public CategoryNotFoundException() {
		super(ErrorType.CATEGORY_NOT_FOUND);
	}
}

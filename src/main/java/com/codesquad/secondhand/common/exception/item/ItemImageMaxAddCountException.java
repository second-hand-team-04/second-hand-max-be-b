package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ItemImageMaxAddCountException extends CustomException {
	public ItemImageMaxAddCountException() {
		super(ErrorType.ITEM_IMAGE_MAX_ADD_COUNT);
	}
}

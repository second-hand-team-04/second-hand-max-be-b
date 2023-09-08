package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ItemImageEmptyException extends CustomException {
	public ItemImageEmptyException() {
		super(ErrorType.ITEM_IMAGE_EMPTY);
	}
}

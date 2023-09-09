package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ItemNotFoundException extends CustomException {

	public ItemNotFoundException() {
		super(ErrorType.ITEM_NOT_FOUND);
	}
}

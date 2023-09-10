package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ItemUnauthorizedUpdateException extends CustomException {

	public ItemUnauthorizedUpdateException() {
		super(ErrorType.ITEM_UNAUTHORIZED_UPDATE);
	}
}

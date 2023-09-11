package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ItemUnauthorizedException extends CustomException {

	public ItemUnauthorizedException() {
		super(ErrorType.ITEM_UNAUTHORIZED);
	}
}

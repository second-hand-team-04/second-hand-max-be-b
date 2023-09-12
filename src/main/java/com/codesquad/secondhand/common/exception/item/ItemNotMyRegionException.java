package com.codesquad.secondhand.common.exception.item;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ItemNotMyRegionException extends CustomException {

	public ItemNotMyRegionException() {
		super(ErrorType.ITEM_NOT_MY_REGION);
	}
}

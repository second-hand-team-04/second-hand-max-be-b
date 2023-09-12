package com.codesquad.secondhand.common.exception.wishlist;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class WishlistNotFoundException extends CustomException {
	public WishlistNotFoundException() {
		super(ErrorType.USER_WISHLIST_ITEM_NOT_FOUND);
	}
}

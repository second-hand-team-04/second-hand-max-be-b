package com.codesquad.secondhand.common.exception.wishlist;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class WishlistNotIncludeException extends CustomException {
	public WishlistNotIncludeException() {
		super(ErrorType.USER_WISHLIST_NOT_INCLUDE);
	}
}

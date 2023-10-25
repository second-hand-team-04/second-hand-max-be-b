package com.codesquad.secondhand.common.exception.wishlist;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class WishlistDuplicationException extends CustomException {
	public WishlistDuplicationException() {
		super(ErrorType.USER_WISHLIST_DUPLICATION);
	}
}

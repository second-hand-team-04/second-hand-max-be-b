package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class InvalidAccountException extends CustomException {
	public InvalidAccountException() {
		super(ErrorType.CHAT_INVALID_ACCOUNT);
	}
}

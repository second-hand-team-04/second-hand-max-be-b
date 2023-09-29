package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class InvalidStompSessionException extends CustomException {
	public InvalidStompSessionException() {
		super(ErrorType.CHAT_INVALID_SESSION);
	}
}

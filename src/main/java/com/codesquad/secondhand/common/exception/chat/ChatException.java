package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ChatException extends CustomException {
	public ChatException(ErrorType errorType) {
		super(errorType);
	}
}

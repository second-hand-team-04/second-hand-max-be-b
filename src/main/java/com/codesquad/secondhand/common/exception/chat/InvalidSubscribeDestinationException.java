package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class InvalidSubscribeDestinationException extends CustomException {
	public InvalidSubscribeDestinationException() {
		super(ErrorType.CHAT_INVALID_SUBSCRIBE_DESTINATION);
	}
}

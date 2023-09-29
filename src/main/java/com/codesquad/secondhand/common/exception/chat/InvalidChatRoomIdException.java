package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class InvalidChatRoomIdException extends CustomException {
	public InvalidChatRoomIdException() {
		super(ErrorType.CHAT_INVALID_ROOM_ID);
	}
}

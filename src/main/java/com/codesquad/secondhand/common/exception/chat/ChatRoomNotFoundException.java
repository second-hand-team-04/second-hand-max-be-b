package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.ErrorType;

public class ChatRoomNotFoundException extends ChatException {
	public ChatRoomNotFoundException() {
		super(ErrorType.CHAT_ROOM_NOT_FOUND);
	}
}

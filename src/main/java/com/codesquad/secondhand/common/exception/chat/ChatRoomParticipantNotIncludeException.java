package com.codesquad.secondhand.common.exception.chat;

import com.codesquad.secondhand.common.exception.CustomException;
import com.codesquad.secondhand.common.exception.ErrorType;

public class ChatRoomParticipantNotIncludeException extends CustomException {
	public ChatRoomParticipantNotIncludeException() {
		super(ErrorType.CHAT_ROOM_PARTICIPANT_NOT_INCLUDE);
	}
}

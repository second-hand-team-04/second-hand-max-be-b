package com.codesquad.secondhand.chat.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomCreateResponse {
	private Long roomId;

	public static ChatRoomCreateResponse from(Long roomId) {
		return new ChatRoomCreateResponse(roomId);
	}
}

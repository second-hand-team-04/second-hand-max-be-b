package com.codesquad.secondhand.chat.application.dto;

import java.io.Serializable;

import com.codesquad.secondhand.chat.domain.ChatMessage;
import com.codesquad.secondhand.chat.domain.ChatRoom;
import com.codesquad.secondhand.user.domain.User;

import lombok.Getter;

@Getter
public class ChatMessageRequest implements Serializable {

	private Long roomId;

	private Long senderId;

	private String content;

	public ChatMessage toChatMessage(ChatRoom chatRoom, User sender) {
		return new ChatMessage(chatRoom, sender, content);
	}
}

package com.codesquad.secondhand.chat.util;

import java.util.Map;
import java.util.Objects;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.common.exception.chat.InvalidAccountException;
import com.codesquad.secondhand.common.exception.chat.InvalidChatRoomIdException;
import com.codesquad.secondhand.common.exception.chat.InvalidStompSessionException;

public class StompHeaderUtil {

	public static StompHeaderAccessor getAccessor(Message<?> message) {
		return StompHeaderAccessor.wrap(message);
	}

	public static Account getAccount(Message<?> message) {
		Object accountAttribute = getSessionAttributes(message).get("account");

		if (Objects.isNull(accountAttribute) || !(accountAttribute instanceof Account)) {
			throw new InvalidAccountException();
		}

		return (Account)accountAttribute;
	}

	public static long getRoomId(Message<?> message) {
		Object chatRoomIdAttribute = getSessionAttributes(message).get("roomId");

		if (Objects.isNull(chatRoomIdAttribute) || !(chatRoomIdAttribute instanceof Long)) {
			throw new InvalidChatRoomIdException();
		}

		return (long)chatRoomIdAttribute;
	}

	public static Map<String, Object> getSessionAttributes(Message<?> message) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (Objects.isNull(sessionAttributes)) {
			throw new InvalidStompSessionException();
		}

		return sessionAttributes;
	}
}

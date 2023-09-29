package com.codesquad.secondhand.chat.handler;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class StompErrorHandler extends StompSubProtocolErrorHandler {

	// private final SubProtocolWebSocketHandler webSocketHandler;

	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
		System.out.println("--------handleClientMessageProcessingError--------");
		System.out.println(clientMessage);

		// return super.handleClientMessageProcessingError(
		// 	createErrorMessage(ex.getCause().getMessage()), ex);
		return createErrorMessage(ex.getCause().getMessage());
	}

	private Message<byte[]> createErrorMessage(String message) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.setLeaveMutable(true);

		return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
	}
}

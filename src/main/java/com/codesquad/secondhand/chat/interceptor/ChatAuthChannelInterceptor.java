package com.codesquad.secondhand.chat.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatAuthChannelInterceptor implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (SimpMessageType.CONNECT.equals(accessor.getMessageType())) {
			System.out.println("stomp connect message sent");
			String token = accessor.getFirstNativeHeader("Authorization");
			// jwtTokenProvider.validateToken(token);
		}

		return ChannelInterceptor.super.preSend(message, channel);
	}

}

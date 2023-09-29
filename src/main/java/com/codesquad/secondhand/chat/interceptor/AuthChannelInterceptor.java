package com.codesquad.secondhand.chat.interceptor;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;
import com.codesquad.secondhand.chat.constants.Constants;
import com.codesquad.secondhand.chat.util.StompHeaderUtil;

import lombok.RequiredArgsConstructor;

@Component
@Order(Constants.Order.CHAT_AUTH_INTERCEPTOR)
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderUtil.getAccessor(message);
		Map<String, Object> sessionAttributes = StompHeaderUtil.getSessionAttributes(message);

		if (SimpMessageType.CONNECT.equals(accessor.getMessageType())) {
			String token = accessor.getFirstNativeHeader("Authorization");
			jwtTokenProvider.validateToken(token);
			sessionAttributes.put("account", jwtTokenProvider.generateAccount(token));
		}

		return ChannelInterceptor.super.preSend(message, channel);
	}
}

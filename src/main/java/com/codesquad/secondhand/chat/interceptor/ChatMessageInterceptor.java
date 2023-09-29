package com.codesquad.secondhand.chat.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.chat.application.ChatMessageService;
import com.codesquad.secondhand.chat.constants.Constants;
import com.codesquad.secondhand.chat.util.StompHeaderUtil;

import lombok.RequiredArgsConstructor;

@Component
@Order(Constants.Order.CHAT_MESSAGE_INTERCEPTOR)
@RequiredArgsConstructor
public class ChatMessageInterceptor implements ChannelInterceptor {

	private final ChatMessageService chatMessageService;

	@Override
	public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderUtil.getAccessor(message);

		return ChannelInterceptor.super.preSend(message, channel);
	}
}

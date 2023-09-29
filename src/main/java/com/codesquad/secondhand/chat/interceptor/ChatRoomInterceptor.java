package com.codesquad.secondhand.chat.interceptor;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.codesquad.secondhand.chat.application.ChatRoomService;
import com.codesquad.secondhand.chat.constants.Constants;
import com.codesquad.secondhand.chat.util.StompHeaderUtil;
import com.codesquad.secondhand.common.exception.chat.InvalidSubscribeDestinationException;

import lombok.RequiredArgsConstructor;

@Component
@Order(Constants.Order.CHAT_ROOM_INTERCEPTOR)
@RequiredArgsConstructor
public class ChatRoomInterceptor implements ChannelInterceptor {

	private final ChatRoomService chatRoomService;

	@Override
	public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderUtil.getAccessor(message);
		Map<String, Object> sessionAttributes = StompHeaderUtil.getSessionAttributes(message);

		if (SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType())) {
			String destination = accessor.getDestination();
			long roomId = extractRoomId(destination);
			validateRoomId(roomId);
			sessionAttributes.put("roomId", roomId);
		}

		return ChannelInterceptor.super.preSend(message, channel);
	}

	private long extractRoomId(String destination) {
		final String DESTINATION_REGEX = "/sub/chat/rooms/[1-9]\\d*";

		if (Objects.isNull(destination) || !Pattern.matches(DESTINATION_REGEX, destination)) {
			throw new InvalidSubscribeDestinationException();
		}

		return Long.parseLong(destination.split("/")[4]);
	}

	private void validateRoomId(long roomId) {
		chatRoomService.findByIdOrThrow(roomId);
	}
}

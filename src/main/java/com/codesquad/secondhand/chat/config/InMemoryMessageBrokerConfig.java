package com.codesquad.secondhand.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.codesquad.secondhand.chat.handler.StompErrorHandler;
import com.codesquad.secondhand.chat.interceptor.AuthChannelInterceptor;
import com.codesquad.secondhand.chat.interceptor.ChatRoomInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class InMemoryMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {

	private final AuthChannelInterceptor authChannelInterceptor;
	private final ChatRoomInterceptor chatValidationChannelInterceptor;
	private final StompErrorHandler stompErrorHandler;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/fish-chat")
			.setAllowedOrigins("*");
		registry.setErrorHandler(stompErrorHandler);
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(authChannelInterceptor);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub");
		registry.setApplicationDestinationPrefixes("/pub");
	}
}

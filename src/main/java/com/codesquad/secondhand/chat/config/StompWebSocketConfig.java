package com.codesquad.secondhand.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.codesquad.secondhand.chat.interceptor.ChatAuthChannelInterceptor;
import com.codesquad.secondhand.chat.interceptor.ChatAuthHandshakeInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final ChatAuthChannelInterceptor chatAuthChannelInterceptor;
	private final ChatAuthHandshakeInterceptor chatAuthHandshakeInterceptor;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/fish-chat")
			.setAllowedOrigins("*")
			.addInterceptors(chatAuthHandshakeInterceptor);
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(chatAuthChannelInterceptor);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub");
		registry.setApplicationDestinationPrefixes("/pub");
	}
}

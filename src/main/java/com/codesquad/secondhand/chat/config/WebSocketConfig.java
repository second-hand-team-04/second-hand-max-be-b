// package com.codesquad.secondhand.chat.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.socket.config.annotation.EnableWebSocket;
// import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
// import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
// import com.codesquad.secondhand.chat.handler.ChatWebSocketHandler;
//
// import lombok.RequiredArgsConstructor;
//
// @Configuration
// @EnableWebSocket
// @RequiredArgsConstructor
// public class WebSocketConfig implements WebSocketConfigurer {
//
// 	private final ChatWebSocketHandler webSocketHandler;
//
// 	@Override
// 	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
// 		registry.addHandler(webSocketHandler, "/fish-chat")
// 			.setAllowedOrigins("*");
// 	}
// }

package com.codesquad.secondhand.chat.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.codesquad.secondhand.auth.infrastrucure.oauth.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatAuthHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Map<String, Object> attributes) throws Exception {
		System.out.println("beforehandshake");
		System.out.println(request.getHeaders());
		String authorization = request.getHeaders().getFirst("Authorization");
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
		Exception exception) {
	}
}

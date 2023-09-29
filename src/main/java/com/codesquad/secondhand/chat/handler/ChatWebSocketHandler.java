// package com.codesquad.secondhand.chat.handler;
//
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;
//
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.CloseStatus;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
//
// @Component
// public class ChatWebSocketHandler extends TextWebSocketHandler {
//
// 	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
// 	@Override
// 	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
// 		System.out.println("----------afterConnectionEstablished---------");
// 		super.afterConnectionEstablished(session);
// 	}
//
// 	@Override
// 	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
// 		System.out.println("---------handleTransportError---------");
// 		super.handleTransportError(session, exception);
// 	}
//
// 	@Override
// 	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
// 		System.out.println("-----------afterConnectionClosed----------");
// 		super.afterConnectionClosed(session, status);
// 	}
// }

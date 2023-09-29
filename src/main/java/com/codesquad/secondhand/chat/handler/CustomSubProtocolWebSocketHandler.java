// package com.codesquad.secondhand.chat.handler;
//
// import org.springframework.messaging.MessageChannel;
// import org.springframework.messaging.SubscribableChannel;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
//
// @Component
// public class CustomSubProtocolWebSocketHandler extends SubProtocolWebSocketHandler {
// 	/**
// 	 * Create a new {@code SubProtocolWebSocketHandler} for the given inbound and outbound channels.
// 	 *
// 	 * @param clientInboundChannel  the inbound {@code MessageChannel}
// 	 * @param clientOutboundChannel the outbound {@code MessageChannel}
// 	 */
// 	public CustomSubProtocolWebSocketHandler(MessageChannel clientInboundChannel,
// 		SubscribableChannel clientOutboundChannel) {
// 		super(clientInboundChannel, clientOutboundChannel);
// 	}
//
// 	@Override
// 	public boolean isAutoStartup() {
// 		return super.isAutoStartup();
// 	}
//
// 	@Override
// 	public int getPhase() {
// 		return super.getPhase();
// 	}
//
// }

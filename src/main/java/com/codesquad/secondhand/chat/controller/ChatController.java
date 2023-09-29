package com.codesquad.secondhand.chat.controller;

import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.codesquad.secondhand.auth.domain.Account;
import com.codesquad.secondhand.chat.application.ChatFacade;
import com.codesquad.secondhand.chat.application.dto.ChatMessageRequest;
import com.codesquad.secondhand.chat.application.dto.ChatRoomCreateRequest;
import com.codesquad.secondhand.common.resolver.AccountPrincipal;
import com.codesquad.secondhand.common.response.CommonResponse;
import com.codesquad.secondhand.common.response.ResponseMessage;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
// @RequestMapping("/chat")
public class ChatController {

	private final ChatFacade chatFacade;

	// TODO 채팅 보내기
	@MessageMapping("/message")
	public void sendMessage(@Payload ChatMessageRequest request) {
		chatFacade.sendMessage(request);
	}

	@MessageMapping("/test")
	public void test() {
		throw new RuntimeException();
	}

	@MessageExceptionHandler()
	public void handleException(Exception e) {
		System.out.println("--------Exception Handler--------" + e);
	}

	// TODO 나의 채팅방 미리보기 목록 조회(Slice)
	// 채팅방 미리보기
	//  - 닉네임
	//  - 가장 최근 채팅 시간
	//  - 가장 최근 채팅 내용
	//  - 안읽은 채팅 수
	//  - 상품 사진

	// TODO 채팅방 생성
	@PostMapping("/chat/rooms")
	public ResponseEntity<CommonResponse> createRoom(@AccountPrincipal Account account,
		@RequestBody ChatRoomCreateRequest request) {
		return ResponseEntity.ok()
			.body(CommonResponse.createOK(
				chatFacade.createRoom(account.getId(), request.getItemId()),
				ResponseMessage.CHAT_ROOM_CREATE
			));
	}

	// TODO 채팅방 입장시 읽음 처리
	@EventListener
	public void handleRoomEnterEvent(SessionSubscribeEvent event) {
		System.out.println("SessionConnectedEvent " + event);
		chatFacade.enterRoom(event);
	}

	// TODO 채팅 받기
	//	    웹소켓 접속 중엔 읽음 처리 되도록

	// TODO 채팅 메세지 GET

	@ConnectMapping("/")
	public void handleTest2() {
		System.out.println("---------connect mapping--------------");
	}

	@SubscribeMapping("/chat/rooms/4")
	public String handleTest() {
		System.out.println("--------------subscribe mapping--------------");
		return "--------------subscribe mapping--------------";
	}

	@MessageExceptionHandler
	public ResponseEntity<CommonResponse> handleJwtException(JwtException e) {
		System.out.println("--------------jwtException Handler-----------------");
		return ResponseEntity.badRequest()
			.body(CommonResponse.createBadRequest("인증실패"));
	}

	@MessageExceptionHandler
	public ResponseEntity<CommonResponse> handleException(RuntimeException e) {
		System.out.println("--------------Exception Handler-----------------");
		return ResponseEntity.badRequest()
			.body(CommonResponse.createBadRequest("예외발생"));
	}
	// @MessageExceptionHandler
	// public ResponseEntity<CommonResponse> handleJwtExcption(JwtException e) {
	// 	return ResponseEntity.badRequest().body(CommonResponse.createBadRequest());
	// }

	// TODO 채팅방 퇴장

}

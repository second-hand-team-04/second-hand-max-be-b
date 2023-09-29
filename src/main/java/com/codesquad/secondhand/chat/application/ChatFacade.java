package com.codesquad.secondhand.chat.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.codesquad.secondhand.chat.application.dto.ChatMessageRequest;
import com.codesquad.secondhand.chat.application.dto.ChatRoomCreateResponse;
import com.codesquad.secondhand.chat.domain.ChatRoom;
import com.codesquad.secondhand.chat.util.StompHeaderUtil;
import com.codesquad.secondhand.item.application.ItemService;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.user.application.UserService;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatFacade {

	private final ChatMessageService chatMessageService;
	private final ChatRoomService chatRoomService;
	private final UserService userService;
	private final ItemService itemService;

	@Transactional
	public void sendMessage(ChatMessageRequest request) {
		ChatRoom chatRoom = chatRoomService.findByIdOrThrow(request.getRoomId());
		User sender = userService.findByIdOrThrow(request.getSenderId());
		chatMessageService.save(request.toChatMessage(chatRoom, sender));
		chatMessageService.send(request);
	}

	@Transactional
	public ChatRoomCreateResponse createRoom(Long buyerId, Long itemId) {
		User buyer = userService.findByIdOrThrow(buyerId);
		Item item = itemService.findByIdOrElseThrow(itemId);

		return chatRoomService.create(buyer, item);
	}

	@Transactional
	public void enterRoom(SessionSubscribeEvent event) {
		long roomId = StompHeaderUtil.getRoomId(event.getMessage());
		long userId = StompHeaderUtil.getAccount(event.getMessage()).getId();
		User user = userService.findByIdOrThrow(userId);
		chatRoomService.enter(roomId, user);
	}

	public void leaveRoom() {
		
	}

}

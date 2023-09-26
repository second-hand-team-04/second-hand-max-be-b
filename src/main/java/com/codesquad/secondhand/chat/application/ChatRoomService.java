package com.codesquad.secondhand.chat.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.chat.application.dto.ChatRoomCreateResponse;
import com.codesquad.secondhand.chat.domain.ChatRoom;
import com.codesquad.secondhand.chat.infrastructure.ChatRoomRepository;
import com.codesquad.secondhand.common.exception.chat.ChatRoomNotFoundException;
import com.codesquad.secondhand.item.domain.Item;
import com.codesquad.secondhand.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;

	public ChatRoom findByIdOrThrow(Long id) {
		return chatRoomRepository.findById(id).orElseThrow(ChatRoomNotFoundException::new);
	}

	public ChatRoomCreateResponse create(User buyer, Item item) {
		ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(item, buyer));

		return ChatRoomCreateResponse.from(chatRoom.getId());
	}
}

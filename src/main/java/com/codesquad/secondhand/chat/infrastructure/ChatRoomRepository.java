package com.codesquad.secondhand.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

package com.codesquad.secondhand.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codesquad.secondhand.chat.domain.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}

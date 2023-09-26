package com.codesquad.secondhand.chat.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codesquad.secondhand.chat.application.dto.ChatMessageRequest;
import com.codesquad.secondhand.chat.domain.ChatMessage;
import com.codesquad.secondhand.chat.infrastructure.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

	@Value("${spring.kafka.template.default-topic}")
	private String kafkaTopic;

	// private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
	private final SimpMessagingTemplate stompTemplate;
	private final ChatMessageRepository chatMessageRepository;

	public void send(ChatMessageRequest request) {
		// kafkaTemplate.send(kafkaTopic, message);
		stompTemplate.convertAndSend("/sub/chat/rooms/" + request.getRoomId(), request);
	}

	public void save(ChatMessage message) {
		chatMessageRepository.save(message);
	}

}

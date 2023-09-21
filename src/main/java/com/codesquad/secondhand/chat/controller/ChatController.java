package com.codesquad.secondhand.chat.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codesquad.secondhand.chat.domain.ChatMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

	public void sendMessage(@RequestBody ChatMessage message) {
	}
}

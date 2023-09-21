package com.codesquad.secondhand.chat.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.codesquad.secondhand.chat.constants.KafkaConstants;
import com.codesquad.secondhand.chat.domain.ChatMessage;

@Configuration
@EnableKafka
public class ListenerConfig {

	public Map<String, Object> consumerConfigurations() {
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER);
		configurations.put(ConsumerConfig.GROUP_ID_CONFIG)
	}

	ConcurrentKafkaListenerContainerFactory<String, ChatMessage> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ChatMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory();
	}
}

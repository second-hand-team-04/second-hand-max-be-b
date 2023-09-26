package com.codesquad.secondhand.chat.config.kafka;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.codesquad.secondhand.chat.domain.ChatMessage;

@Configuration
@EnableKafka
public class ListenerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.consumer.group-id}")
	private String consumerGroupId;

	@Value("${spring.kafka.consumer.key-deserializer")
	private String keyDeserializer;

	@Value("${spring.kafka.consumer.value-deserializer")
	private String valueDeserializer;

	@Bean
	public ConsumerFactory<String, ChatMessage> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigurations(), new StringDeserializer(),
			new org.springframework.kafka.support.serializer.JsonDeserializer<>(ChatMessage.class));
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, ChatMessage> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ChatMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public Map<String, Object> consumerConfigurations() {
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configurations.put(GROUP_ID_CONFIG, consumerGroupId);
		configurations.put(KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
		configurations.put(VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
		configurations.put(AUTO_OFFSET_RESET_CONFIG, "latest");
		return configurations;
	}
}

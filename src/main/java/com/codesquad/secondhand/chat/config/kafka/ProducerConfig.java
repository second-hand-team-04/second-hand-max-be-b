package com.codesquad.secondhand.chat.config.kafka;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.codesquad.secondhand.chat.domain.ChatMessage;

@Configuration
@EnableKafka
public class ProducerConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;

	@Value("${spring.kafka.producer.key-serializer")
	private String keySerializer;

	@Value("${spring.kafka.producer.value-serializer")
	private String valueSerializer;

	@Bean
	public ProducerFactory<String, ChatMessage> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigurations());
	}

	@Bean
	public Map<String, Object> producerConfigurations() {
		Map<String, Object> configurations = new HashMap<>();
		configurations.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configurations.put(KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
		configurations.put(VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
		return configurations;
	}

	@Bean
	public KafkaTemplate<String, ChatMessage> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}

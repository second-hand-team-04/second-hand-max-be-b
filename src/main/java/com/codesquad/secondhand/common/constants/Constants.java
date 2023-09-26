package com.codesquad.secondhand.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Constants {

	@Getter
	@RequiredArgsConstructor
	public enum Kafka {
		CHAT_TOPIC("chat_messages"),
		CHAT_CONSUMER_GROUP("chat_consumers"),
		BROKER("localhost:9092");

		private final String value;
	}
}

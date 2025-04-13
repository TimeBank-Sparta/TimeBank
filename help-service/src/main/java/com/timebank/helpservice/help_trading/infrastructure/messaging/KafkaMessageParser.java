package com.timebank.helpservice.help_trading.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaMessageParser {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static <T> T parse(Object message, Class<T> targetClass) {
		try {
			String json;
			if (message instanceof String) {
				json = (String)message;
			} else {
				json = objectMapper.writeValueAsString(message);
			}
			return objectMapper.readValue(json, targetClass);
		} catch (Exception e) {
			throw new RuntimeException("Kafka message parsing failed", e);
		}
	}
}


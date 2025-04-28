package com.timebank.helpservice.helper.infrastructure.messaing;

import com.fasterxml.jackson.databind.ObjectMapper;

//TODO global util로 두던가 해야할듯
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


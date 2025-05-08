package com.timebank.notification_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SlackWebHookMessage {
	private String text;

	public static SlackWebHookMessage from(String text) {
		return SlackWebHookMessage.builder()
			.text(text)
			.build();
	}
}

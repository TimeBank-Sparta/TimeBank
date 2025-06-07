package com.timebank.notification_service.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SlackBotMessage {
	private String channel;
	private String text;

	public static SlackBotMessage of(String channel, String text) {
		return SlackBotMessage.builder()
			.channel(channel)
			.text(text)
			.build();
	}
}
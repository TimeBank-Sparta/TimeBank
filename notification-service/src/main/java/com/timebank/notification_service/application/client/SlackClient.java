package com.timebank.notification_service.application.client;

import com.timebank.notification_service.application.dto.SlackBotMessage;
import com.timebank.notification_service.application.dto.SlackWebHookMessage;

public interface SlackClient {
	void sendMessage(SlackWebHookMessage message);

	void sendMessage(SlackBotMessage request);

	String getUserIdByEmail(String email);

}

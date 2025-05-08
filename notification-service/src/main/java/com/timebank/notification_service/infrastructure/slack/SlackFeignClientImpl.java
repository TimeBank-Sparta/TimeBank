package com.timebank.notification_service.infrastructure.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.timebank.notification_service.application.client.SlackClient;
import com.timebank.notification_service.application.dto.SlackBotMessage;
import com.timebank.notification_service.application.dto.SlackUserResponse;
import com.timebank.notification_service.application.dto.SlackWebHookMessage;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SlackFeignClientImpl implements SlackClient {
	private final SlackWebHookClient slackWebHookClient;
	private final SlackBotClient slackBotClient;
	private final String bearerPrefix = "Bearer ";

	@Value("${slack.bot.token}")
	private String slackBotToken;

	@Override
	@Retry(name = "slackMessageRetry", fallbackMethod = "sendMessageFallback")
	public void sendMessage(SlackWebHookMessage message) {
		slackWebHookClient.sendMessage(message);
	}

	@Override
	@Retry(name = "slackMessageRetry", fallbackMethod = "sendMessageFallback")
	public void sendMessage(SlackBotMessage body) {
		slackBotClient.sendMessage(bearerPrefix + slackBotToken, body);
	}

	@Override
	@Retry(name = "slackMessageRetry", fallbackMethod = "lookupByEmailFallback")
	public String getUserIdByEmail(String email) {
		SlackUserResponse response = slackBotClient.lookupByEmail(bearerPrefix + slackBotToken, email);
		return response.getUser().getId();
	}

	private void sendMessageFallback(SlackWebHookMessage message, Throwable t) {
		log.error("SlackWebHook 메시지 전송 실패. fallback 실행: {}", t.getMessage());
	}

	private void sendMessageFallback(SlackBotMessage message, Throwable t) {
		log.error("SlackBot 메시지 전송 실패. fallback 실행: {}", t.getMessage());
	}

	private String lookupByEmailFallback(String email, Throwable t) {
		log.error("lookupByEmail 실패. fallback 실행: {}", t.getMessage());
		return null;
	}

}

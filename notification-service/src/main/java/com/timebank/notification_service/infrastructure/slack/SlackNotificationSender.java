package com.timebank.notification_service.infrastructure.slack;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SlackNotificationSender {

	@Value("${slack.webhook.url}")
	private String webhookUrl;

	private final RestTemplate restTemplate;
	private final UserRepository userRepository;

	public void send(Long userId, String message, Map<String, String> payload) {
		String slackId = userRepository.findSlackIdByUserId(userId);

		String fullMessage = String.format("<@%s> %s", slackId, message);

		Map<String, Object> slackMessage = new HashMap<>();
		slackMessage.put("text", fullMessage);

		try {
			restTemplate.postForEntity(webhookUrl, slackMessage, String.class);
			log.info("Slack 알림 발송 완료 (userId: {})", userId);
		} catch (Exception e) {
			log.error("Slack 알림 발송 실패: {}", e.getMessage());
		}
	}
}

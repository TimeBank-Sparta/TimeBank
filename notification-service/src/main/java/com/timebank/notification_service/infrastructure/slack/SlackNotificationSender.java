// package com.timebank.notification_service.infrastructure.slack;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestTemplate;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Component
// @Slf4j
// @RequiredArgsConstructor
// public class SlackNotificationSender {
//
// 	private final RestTemplate restTemplate;
//
// 	@Value("${slack.webhook.url}")
// 	private String webhookUrl;
//
// 	public void send(String slackUserId, String message) {
// 		String payload = String.format("{\"text\": \"<@%s> %s\"}", slackUserId, message);
//
// 		HttpHeaders headers = new HttpHeaders();
// 		headers.setContentType(MediaType.APPLICATION_JSON);
//
// 		HttpEntity<String> request = new HttpEntity<>(payload, headers);
//
// 		try {
// 			restTemplate.postForEntity(webhookUrl, request, String.class);
// 			log.info("슬랙 알림 발송 완료: {}", slackUserId);
// 		} catch (Exception e) {
// 			log.error("슬랙 알림 발송 실패: {}", e.getMessage());
// 		}
// 	}
// }

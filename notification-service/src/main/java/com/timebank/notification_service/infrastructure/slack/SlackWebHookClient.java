package com.timebank.notification_service.infrastructure.slack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.timebank.notification_service.application.dto.SlackWebHookMessage;

@FeignClient(name = "slackWebhookClient", url = "${slack.webhook.url}")
public interface SlackWebHookClient {

	@PostMapping("${slack.webhook.path}")
	void sendMessage(@RequestBody SlackWebHookMessage message);
}

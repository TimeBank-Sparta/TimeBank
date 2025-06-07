package com.timebank.notification_service.infrastructure.slack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.timebank.notification_service.application.dto.SlackBotMessage;
import com.timebank.notification_service.application.dto.SlackUserResponse;

@FeignClient(name = "slackBotClient", url = "https://slack.com/api")
public interface SlackBotClient {

	@PostMapping("/chat.postMessage")
	void sendMessage(@RequestHeader("Authorization") String authorization,
		@RequestBody SlackBotMessage body);

	@GetMapping("/users.lookupByEmail")
	SlackUserResponse lookupByEmail(@RequestHeader("Authorization") String authorization,
		@RequestParam("email") String email);
}

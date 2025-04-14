package com.timebank.common.infrastructure.externel.notification;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.common.infrastructure.externel.notification.dto.NotificationDto;

@FeignClient(name = "notification-service", path = "/api/v1/users/{userId}/notifications")
public interface UserNotificationClient {

	@GetMapping
	ResponseDto<List<NotificationDto>> getUserNotifications(@PathVariable("userId") Long userId);
}
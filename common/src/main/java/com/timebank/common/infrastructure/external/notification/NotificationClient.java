package com.timebank.common.infrastructure.external.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.common.infrastructure.config.FeignConfig;
import com.timebank.common.infrastructure.external.notification.dto.NotificationDto;

@FeignClient(name = "notification-service", path = "/api/v1/notifications", configuration = FeignConfig.class)
public interface NotificationClient {

	@GetMapping
	PageResponseDto<NotificationDto> getAllNotifications(Pageable pageable);

	@GetMapping("/{notificationId}")
	ResponseDto<NotificationDto> getNotification(@PathVariable("notificationId") Long notificationId);

	@PatchMapping("/{notificationId}/read")
	ResponseDto<NotificationDto> markNotificationAsRead(@PathVariable("notificationId") Long notificationId);

	@DeleteMapping("/{notificationId}")
	ResponseDto<String> deleteNotification(@PathVariable("notificationId") Long notificationId);
}
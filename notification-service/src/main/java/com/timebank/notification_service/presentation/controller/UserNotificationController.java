package com.timebank.notification_service.presentation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.ResponseDto;
import com.timebank.notification_service.application.dto.NotificationDto;
import com.timebank.notification_service.application.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/notifications/users/{userId}")
@RequiredArgsConstructor
public class UserNotificationController {

	private final NotificationService notificationService;

	/**
	 * 특정 사용자의 알림 조회
	 * GET /api/v1/users/{userId}/notifications
	 */
	@GetMapping
	public ResponseEntity<ResponseDto<List<NotificationDto>>> getUserNotifications(
		@PathVariable Long userId) {
		List<NotificationDto> notifications = notificationService.getNotificationsByUser(userId);
		return ResponseEntity.ok(ResponseDto.success(notifications));
	}
}
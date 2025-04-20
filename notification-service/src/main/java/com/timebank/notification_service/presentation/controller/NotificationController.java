package com.timebank.notification_service.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timebank.common.application.dto.PageResponseDto;
import com.timebank.common.application.dto.ResponseDto;
import com.timebank.notification_service.application.dto.NotificationDto;
import com.timebank.notification_service.application.service.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	/**
	 * 테스트 알림 생성
	 * POST /api/v1/notifications
	 */
	@PostMapping
	public ResponseEntity<ResponseDto<NotificationDto>> createNotification(
		@RequestBody @Valid NotificationDto notificationDto) {
		NotificationDto created = notificationService.createNotification(notificationDto);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ResponseDto.success(HttpStatus.CREATED, created));
	}

	/**
	 * 전체 알림 조회
	 * GET /api/v1/notifications
	 */
	@GetMapping
	public ResponseEntity<PageResponseDto<NotificationDto>> getAllNotifications(Pageable pageable) {
		Page<NotificationDto> notificationsPage = notificationService.getAllNotifications(pageable);
		// PageResponseDto.success(HttpStatus status, Page<T> page, String message) 사용
		PageResponseDto<NotificationDto> responseDto = PageResponseDto.success(
			HttpStatus.OK, notificationsPage, "Notifications fetched successfully");
		return ResponseEntity.status(HttpStatus.OK)
			.body(responseDto);
	}

	/**
	 * 특정 알림 상세 조회
	 * GET /api/v1/notifications/{notificationId}
	 */
	@GetMapping("/{notificationId}")
	public ResponseEntity<ResponseDto<NotificationDto>> getNotification(
		@PathVariable Long notificationId) {
		NotificationDto notification = notificationService.getNotification(notificationId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, notification));
	}

	/**
	 * 알림 상태 업데이트 (읽음 처리)
	 * PATCH /api/v1/notifications/{notificationId}/read
	 */
	@PatchMapping("/{notificationId}")
	public ResponseEntity<ResponseDto<NotificationDto>> markNotificationAsRead(
		@PathVariable Long notificationId) {
		NotificationDto updatedNotification = notificationService.markAsRead(notificationId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, updatedNotification));
	}

	/**
	 * 알림 삭제
	 * DELETE /api/v1/notifications/{notificationId}
	 */
	@DeleteMapping("/{notificationId}")
	public ResponseEntity<ResponseDto<String>> deleteNotification(
		@PathVariable Long notificationId) {
		notificationService.deleteNotification(notificationId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseDto.success(HttpStatus.OK, "Notification deleted successfully"));
	}
}

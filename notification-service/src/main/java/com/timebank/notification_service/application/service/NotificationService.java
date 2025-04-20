package com.timebank.notification_service.application.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.notification_service.application.dto.NotificationDto;
import com.timebank.notification_service.application.event.NotificationEvent;
import com.timebank.notification_service.domain.entity.Notification;
import com.timebank.notification_service.domain.entity.NotificationEventType;
import com.timebank.notification_service.domain.repository.NotificationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * 알림 생성
	 * POST /api/v1/notifications
	 */
	public NotificationDto createNotification(NotificationDto notificationDto) {
		// 엔티티 생성 및 저장
		Notification notification = Notification.builder()
			.recipientId(notificationDto.getRecipientId())
			.senderId(notificationDto.getSenderId())
			.notificationType(notificationDto.getNotificationType())
			.eventType(NotificationEventType.CREATED)
			.message(notificationDto.getMessage())
			.isRead(Boolean.FALSE)
			.build();
		notification = notificationRepository.save(notification);

		// Kafka 이벤트 발행 (CREATED)
		NotificationEvent event = new NotificationEvent(notification, NotificationEventType.CREATED);
		kafkaTemplate.send(NotificationEventType.CREATED.getTopic(), event);

		return NotificationDto.fromEntity(notification);
	}

	/**
	 * 전체 알림 조회 (페이지네이션 적용)
	 */
	public Page<NotificationDto> getAllNotifications(Pageable pageable) {
		Page<Notification> notifications = notificationRepository.findAll(pageable);
		// 빈 페이지도 정상 반환 (404가 아닌 200 + 빈 리스트)
		return notifications.map(NotificationDto::fromEntity);
	}

	/**
	 * 특정 알림 상세 조회
	 */
	public NotificationDto getNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException(
				"해당 ID의 알림을 찾을 수 없습니다. id=" + notificationId));
		return NotificationDto.fromEntity(notification);
	}

	/**
	 * 알림 상태 업데이트 (읽음 처리)
	 */
	public NotificationDto markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException(
				"해당 ID의 알림을 찾을 수 없습니다. id=" + notificationId));

		if (Boolean.TRUE.equals(notification.getIsRead())) {
			throw new IllegalStateException("이미 읽음 처리된 알림입니다. id=" + notificationId);
		}

		notification.setIsRead(true);
		notification = notificationRepository.save(notification);

		// Kafka 이벤트 발행 (UPDATED)
		NotificationEvent event = new NotificationEvent(notification, NotificationEventType.UPDATED);
		kafkaTemplate.send(NotificationEventType.UPDATED.getTopic(), event);

		return NotificationDto.fromEntity(notification);
	}

	/**
	 * 알림 삭제
	 */
	public void deleteNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException(
				"해당 ID의 알림을 찾을 수 없습니다. id=" + notificationId));

		notificationRepository.delete(notification);

		// Kafka 이벤트 발행 (DELETED)
		NotificationEvent event = new NotificationEvent(notification, NotificationEventType.DELETED);
		kafkaTemplate.send(NotificationEventType.DELETED.getTopic(), event);
	}

	/**
	 * 특정 사용자의 알림 조회
	 */
	public List<NotificationDto> getNotificationsByUser(Long userId) {
		List<Notification> notifications = notificationRepository.findByRecipientId(userId);
		if (notifications.isEmpty()) {
			// 반환 시 빈 리스트 OK, 혹은 404를 원하면 예외를 던지도록 변경
			return Collections.emptyList();
		}
		return notifications.stream()
			.map(NotificationDto::fromEntity)
			.collect(Collectors.toList());
	}
}

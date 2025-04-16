package com.timebank.notification_service.application.service;

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
	// 기본 토픽은 여러 이벤트가 발행될 때 하나의 기본 토픽을 사용할 수 있으나,
	// 이벤트 타입에 따라 실제 발행 시에는 각 이벤트의 토픽을 참고할 수 있음.
	private final String notificationTopic = "notification.events";

	/**
	 * 전체 알림 조회 (페이지네이션 적용)
	 */
	public Page<NotificationDto> getAllNotifications(Pageable pageable) {
		Page<Notification> notifications = notificationRepository.findAll(pageable);
		return notifications.map(NotificationDto::fromEntity);
	}

	/**
	 * 특정 알림 상세 조회
	 */
	public NotificationDto getNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));
		return NotificationDto.fromEntity(notification);
	}

	/**
	 * 알림 상태 업데이트 (읽음 처리)
	 */
	public NotificationDto markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));
		notification.setIsRead(true);
		notification = notificationRepository.save(notification);

		// Kafka 이벤트 발행: UPDATED 이벤트
		NotificationEvent event = new NotificationEvent(notification, NotificationEventType.UPDATED);
		kafkaTemplate.send(notificationTopic, event);

		return NotificationDto.fromEntity(notification);
	}

	/**
	 * 알림 삭제
	 */
	public void deleteNotification(Long notificationId) {
		if (!notificationRepository.existsById(notificationId)) {
			throw new EntityNotFoundException("Notification not found with id: " + notificationId);
		}
		Notification notification = notificationRepository.findById(notificationId).get();
		notificationRepository.deleteById(notificationId);

		// Kafka 이벤트 발행: DELETED 이벤트
		NotificationEvent event = new NotificationEvent(notification, NotificationEventType.DELETED);
		kafkaTemplate.send(notificationTopic, event);
	}

	/**
	 * 특정 사용자의 알림 조회
	 */
	public List<NotificationDto> getNotificationsByUser(Long userId) {
		List<Notification> notifications = notificationRepository.findByRecipientId(userId);
		return notifications.stream()
			.map(NotificationDto::fromEntity)
			.collect(Collectors.toList());
	}
}

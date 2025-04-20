package com.timebank.notification_service.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.timebank.notification_service.application.event.NotificationEvent;
import com.timebank.notification_service.domain.entity.Notification;
import com.timebank.notification_service.domain.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationRepository notificationRepository;

	// CREATED 토픽 소비 메서드
	@KafkaListener(topics = "notification.events.CREATED", groupId = "notification-group")
	public void consumeCreated(NotificationEvent event) {
		log.info("Consumed CREATED event: {}", event);
		saveNotification(event);
		// 필요시 추가 로직 처리 (예: 로깅)
	}

	// UPDATED 토픽 소비 메서드
	@KafkaListener(topics = "notification.events.UPDATED", groupId = "notification-group")
	public void consumeUpdated(NotificationEvent event) {
		log.info("Consumed UPDATED event: {}", event);
		// 업데이트 이벤트의 경우 추가 처리 로직이 있다면 구현합니다.
		saveNotification(event);
	}

	// DELETED 토픽 소비 메서드
	@KafkaListener(topics = "notification.events.DELETED", groupId = "notification-group")
	public void consumeDeleted(NotificationEvent event) {
		log.info("Consumed DELETED event: {}", event);
		// 삭제 이벤트는 삭제 후 로깅 혹은 관련 작업 수행
		saveNotification(event);
	}

	// 공통적으로 Notification DB 저장 처리
	private void saveNotification(NotificationEvent event) {
		Notification notification = Notification.builder()
			.recipientId(event.getRecipientId())
			.notificationType(event.getType())
			.eventType(event.getEventType())
			.message(event.getMessage())
			.isRead(false)
			.build();
		notificationRepository.save(notification);
		log.info("Notification saved: {}", notification);
	}
}


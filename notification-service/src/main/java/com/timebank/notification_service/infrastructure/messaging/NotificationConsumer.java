package com.timebank.notification_service.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.timebank.notification_service.application.event.NotificationEvent;
import com.timebank.notification_service.domain.entity.Notification;
import com.timebank.notification_service.domain.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

	private final NotificationRepository notificationRepository;

	@KafkaListener(topics = "notification-topic", groupId = "notification-group")
	public void consume(NotificationEvent event) {
		log.info("Notification Event Consumed: {}", event);

		// 알림 생성 및 저장
		Notification notification = Notification.builder()
			.userId(event.getUserId())
			.message(event.getMessage())
			.notificationType(event.getNotificationType())
			.isRead(false)
			.createdAt(event.getTimestamp())
			.build();

		notificationRepository.save(notification);
	}
}

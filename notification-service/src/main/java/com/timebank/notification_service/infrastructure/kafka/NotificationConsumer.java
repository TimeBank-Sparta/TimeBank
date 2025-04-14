package com.timebank.notification_service.infrastructure.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
	private final SlackNotificationSender slackNotificationSender;
	private final EmailNotificationSender emailNotificationSender;

	@KafkaListener(topics = "notification-topic", groupId = "notification-group")
	public void consume(NotificationEvent event, Acknowledgment acknowledgment) {
		try {
			// 1. DB에 알림 저장
			Notification notification = Notification.builder()
				.userId(event.getUserId())
				.message(event.getMessage())
				.notificationType(event.getNotificationType())
				.isRead(false)
				.createdAt(event.getTimestamp())
				.build();
			notificationRepository.save(notification);

			// 2. Slack 발송
			slackNotificationSender.send(event.getUserId(), event.getMessage(), event.getPayload());

			// 3. Email 발송
			emailNotificationSender.send(event.getUserId(), event.getMessage(), event.getPayload());

			acknowledgment.acknowledge();  // Ack 처리
		} catch (Exception e) {
			log.error("Notification 처리 중 오류: {}", e.getMessage());
		}
	}
}
package com.timebank.notification_service.application.event;

import java.time.LocalDateTime;

import com.timebank.notification_service.domain.entity.Notification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationEvent {
	private Long notificationId;
	private Long recipientId;
	private String type;
	private String message;
	private Boolean isRead;
	private LocalDateTime sentAt;
	private String eventType; // 예: CREATED, UPDATED, DELETED

	public NotificationEvent(Notification notification, String eventType) {
		this.notificationId = notification.getNotificationId();
		this.recipientId = notification.getRecipientId();
		this.type = notification.getNotificationType().toString();
		this.message = notification.getMessage();
		this.isRead = notification.getIsRead();
		this.eventType = eventType;
	}
}

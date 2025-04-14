package com.timebank.notification_service.application.event;

import java.time.LocalDateTime;
import java.util.Map;

import com.timebank.notification_service.domain.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
	private Long notificationId;
	private Long recipientId;
	private String type;
	private String message;
	private Boolean isRead;
	private LocalDateTime sentAt;
	private String eventType;
	//  (사용자 디바이스 알림 전달용 필드)
	private Map<String, String> payload;

	public NotificationEvent(Notification notification, String eventType) {
		this.notificationId = notification.getNotificationId();
		this.recipientId = notification.getRecipientId();
		this.type = notification.getNotificationType();
		this.message = notification.getMessage();
		this.isRead = notification.getIsRead();
		this.eventType = eventType;
	}
}

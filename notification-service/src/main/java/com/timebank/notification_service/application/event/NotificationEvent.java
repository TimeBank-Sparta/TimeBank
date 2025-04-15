package com.timebank.notification_service.application.event;

import java.time.LocalDateTime;
import java.util.Map;

import com.timebank.notification_service.domain.entity.Notification;
import com.timebank.notification_service.domain.entity.NotificationEventType;
import com.timebank.notification_service.domain.entity.NotificationType;

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
	private NotificationType type;         // 알림의 타입 (예: USER_LOGIN, POINT_HOLD 등)
	private String message;
	private Boolean isRead;
	private LocalDateTime sentAt;
	private NotificationEventType eventType; // 이벤트 구분 (CREATED, UPDATED, DELETED)
	private Map<String, String> payload;     // (추가 정보 전달용)

	// 생성자: Notification 엔티티와 이벤트 타입 enum을 받아서 생성
	public NotificationEvent(Notification notification, NotificationEventType eventType) {
		this.notificationId = notification.getNotificationId();
		this.recipientId = notification.getRecipientId();
		this.type = notification.getNotificationType();
		this.message = notification.getMessage();
		this.isRead = notification.getIsRead();
		this.eventType = eventType;
		this.sentAt = LocalDateTime.now();
	}
}

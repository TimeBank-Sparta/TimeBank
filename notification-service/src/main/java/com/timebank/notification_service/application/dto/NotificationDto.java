package com.timebank.notification_service.application.dto;

import com.timebank.notification_service.domain.entity.Notification;
import com.timebank.notification_service.domain.entity.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
	private Long notificationId;
	private Long recipientId;
	private Long senderId; // 시스템 알림의 경우 null
	private NotificationType notificationType; // enum 타입으로 변경
	private String message;
	private Boolean isRead;

	public static NotificationDto fromEntity(Notification notification) {
		return new NotificationDto(
			notification.getNotificationId(),
			notification.getRecipientId(),
			notification.getSenderId(),
			notification.getNotificationType(),
			notification.getMessage(),
			notification.getIsRead()
		);
	}
}

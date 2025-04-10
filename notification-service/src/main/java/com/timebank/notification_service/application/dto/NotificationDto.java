package com.timebank.notification_service.application.dto;

import java.time.LocalDateTime;

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
	private Long senderId; // nullable
	private NotificationType notificationType;
	private String message;
	private Boolean isRead;
	private LocalDateTime sentAt;

	public static NotificationDto fromEntity(Notification notification) {
		return new NotificationDto(
			notification.getNotificationId(),
			notification.getRecipientId(),
			notification.getSenderId(),
			notification.getNotificationType(),
			notification.getMessage(),
			notification.getIsRead(),
			notification.getSentAt()
		);
	}

}

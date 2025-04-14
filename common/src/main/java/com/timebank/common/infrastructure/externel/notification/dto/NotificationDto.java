package com.timebank.common.infrastructure.externel.notification.dto;

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
	private String notificationType;
	private String message;
	private Boolean isRead;

}

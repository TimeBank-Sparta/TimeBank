package com.timebank.notification_service.application.dto;

import com.timebank.common.infrastructure.external.notification.dto.NotificationType;
import com.timebank.notification_service.domain.entity.Notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

	@NotNull(message = "수신자 ID는 필수입니다.")
	private Long recipientId;

	// 시스템 알림은 null 허용
	private Long senderId;

	@NotNull(message = "알림 유형은 필수입니다.")
	private NotificationType notificationType;

	@NotBlank(message = "메시지는 공백일 수 없습니다.")
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

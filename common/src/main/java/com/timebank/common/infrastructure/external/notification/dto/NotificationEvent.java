package com.timebank.common.infrastructure.external.notification.dto;

import java.time.LocalDateTime;
import java.util.Map;

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
	private String slackUserEmail;
}

package com.timebank.notification_service.domain.entity;

import com.timebank.common.domain.Timestamped;
import com.timebank.common.infrastructure.external.notification.dto.NotificationEventType;
import com.timebank.common.infrastructure.external.notification.dto.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
public class Notification extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long notificationId;

	@Column(name = "recipient_id", nullable = false)
	private Long recipientId;

	// 발신자 ID, 시스템 알림일 경우 null 가능
	@Column(name = "sender_id")
	private Long senderId;

	// 알림 유형: 기존 문자열 대신 NotificationType enum 사용
	@Enumerated(EnumType.STRING)
	@Column(name = "notification_type", nullable = false)
	private NotificationType notificationType;

	// 이벤트 타입: Kafka 메시지 이벤트 등에서 사용할 용도로 NotificationEventType enum 사용
	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	private NotificationEventType eventType;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String message;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead = false;

	// 기본 생성자 외에 편의를 위한 생성자 (eventType 없이)
	public Notification(Long recipientId, Long senderId, NotificationType notificationType, String message) {
		this.recipientId = recipientId;
		this.senderId = senderId;
		this.notificationType = notificationType;
		this.message = message;
		this.isRead = false;
	}

	// NotificationType과 NotificationEventType 정보를 모두 사용하는 생성자
	public Notification(Long recipientId, Long senderId, NotificationType notificationType,
		NotificationEventType eventType, String message) {
		this.recipientId = recipientId;
		this.senderId = senderId;
		this.notificationType = notificationType;
		this.eventType = eventType;
		this.message = message;
		this.isRead = false;
	}
}
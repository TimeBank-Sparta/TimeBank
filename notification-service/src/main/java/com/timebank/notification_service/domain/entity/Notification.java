package com.timebank.notification_service.domain.entity;

import com.timebank.common.domain.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notifications")
public class Notification extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long notificationId;

	@Column(name = "recipient_id", nullable = false)
	private Long recipientId;

	// 발신자 ID는 시스템 알림일 경우 null 가능
	@Column(name = "sender_id")
	private Long senderId;

	@Column(name = "notification_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String message;

	@Column(name = "is_read", nullable = false)
	private Boolean isRead = false;

	public Notification(Long recipientId, Long senderId, NotificationType notificationType, String message) {
		this.recipientId = recipientId;
		this.senderId = senderId;
		this.notificationType = notificationType;
		this.message = message;
		this.isRead = false;
	}
}
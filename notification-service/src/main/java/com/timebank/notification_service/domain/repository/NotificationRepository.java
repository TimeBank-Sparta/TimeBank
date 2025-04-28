package com.timebank.notification_service.domain.repository;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.notification_service.domain.entity.Notification;

@Configuration
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	// 특정 사용자의 알림 조회
	List<Notification> findByRecipientId(Long recipientId);
}
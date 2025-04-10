package com.timebank.notification_service.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timebank.notification_service.application.dto.NotificationDto;

@Service
public class NotificationService {
	public List<NotificationDto> getAllNotifications() {
	}

	public NotificationDto getNotification(Long notificationId) {
	}

	public NotificationDto markAsRead(Long notificationId) {
	}

	public void deleteNotification(Long notificationId) {
	}

	public List<NotificationDto> getNotificationsByUser(Long userId) {
	}
}

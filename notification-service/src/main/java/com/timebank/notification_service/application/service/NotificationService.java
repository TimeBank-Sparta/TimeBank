package com.timebank.notification_service.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.notification_service.application.dto.NotificationDto;
import com.timebank.notification_service.domain.entity.Notification;
import com.timebank.notification_service.domain.repository.NotificationRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;

	public List<NotificationDto> getAllNotifications() {
		List<Notification> notifications = notificationRepository.findAll();
		return notifications.stream()
			.map(NotificationDto::fromEntity)
			.collect(Collectors.toList());
	}

	public NotificationDto getNotification(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));
		return NotificationDto.fromEntity(notification);
	}

	public NotificationDto markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
			.orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + notificationId));
		notification.setIsRead(true);
		notification = notificationRepository.save(notification);
		return NotificationDto.fromEntity(notification);
	}

	public void deleteNotification(Long notificationId) {
		if (!notificationRepository.existsById(notificationId)) {
			throw new EntityNotFoundException("Notification not found with id: " + notificationId);
		}
		notificationRepository.deleteById(notificationId);
	}

	public List<NotificationDto> getNotificationsByUser(Long userId) {
		List<Notification> notifications = notificationRepository.findByRecipientId(userId);
		return notifications.stream()
			.map(NotificationDto::fromEntity)
			.collect(Collectors.toList());
	}
}
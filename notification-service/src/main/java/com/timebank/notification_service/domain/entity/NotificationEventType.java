package com.timebank.notification_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationEventType {
	CREATED("CREATED"),
	UPDATED("UPDATED"),
	DELETED("DELETED");
	private final String string;
}

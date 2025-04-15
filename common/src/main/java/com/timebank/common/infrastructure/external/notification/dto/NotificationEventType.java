package com.timebank.common.infrastructure.external.notification.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationEventType {
	CREATED("notification.events.CREATED"),
	UPDATED("notification.events.UPDATED"),
	DELETED("notification.events.DELETED");

	private final String topic;

}

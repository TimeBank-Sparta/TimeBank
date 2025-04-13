package com.timebank.notification_service.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
	SERVICE_REQUEST("SERVICE_REQUEST"),
	TRANSACTION_UPDATE("TRANSACTION_UPDATE"),
	POINT_UPDATE("POINT_UPDATE"),
	ADMIN_NOTICE("ADMIN_NOTICE");
	private final String string;
}
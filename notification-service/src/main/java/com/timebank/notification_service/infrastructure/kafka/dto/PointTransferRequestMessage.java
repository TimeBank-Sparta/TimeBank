package com.timebank.notification_service.infrastructure.kafka.dto;

public record PointTransferRequestMessage(
	Long senderUserId,
	Long receiverUserId,
	int amount,
	String reason
) {
}
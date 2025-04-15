package com.timebank.pointservice.infrastructure.kafka.dto;

public record PointTransferRequestMessage(
	Long senderUserId,
	Long receiverUserId,
	int amount
) {
}
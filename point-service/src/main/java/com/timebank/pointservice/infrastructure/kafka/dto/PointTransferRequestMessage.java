package com.timebank.pointservice.infrastructure.kafka.dto;

public record PointTransferRequestMessage(
	Long senderAccountId,
	Long receiverAccountId,
	int amount,
	String reason
) {}
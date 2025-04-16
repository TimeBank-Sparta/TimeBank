package com.timebank.pointservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointTransferCommand {
	Long senderUserId;
	Long receiverUserId;
	int amount;

	public PointTransferCommand(Long senderAccountId, Long receiverAccountId, Integer amount, String reason) {
	}
}

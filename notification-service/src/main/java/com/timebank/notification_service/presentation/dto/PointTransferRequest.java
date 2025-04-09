package com.timebank.notification_service.presentation.dto;

import com.timebank.pointservice.application.dto.PointTransferCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransferRequest {
	private Long senderAccountId;
	private Long receiverAccountId;
	private Integer amount;
	private String reason;

	public PointTransferCommand toCommand() {
		return new PointTransferCommand(senderAccountId, receiverAccountId, amount, reason);
	}

}

package com.timebank.pointservice.presentation.dto;

import com.timebank.pointservice.application.dto.PointTransferCommand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransferRequest {
	private Long senderUserId;
	private Long receiverUserId;
	private Integer amount;
	private String reason;

	public PointTransferCommand toCommand() {
		return new PointTransferCommand(senderUserId, receiverUserId, amount, reason);
	}

}

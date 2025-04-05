package com.timebank.pointservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointTransferCommand {
	private Long senderAccountId;
	private Long receiverAccountId;
	private Integer amount;
	private String reason;
}

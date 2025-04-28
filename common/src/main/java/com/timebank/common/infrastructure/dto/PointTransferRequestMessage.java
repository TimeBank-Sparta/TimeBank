package com.timebank.common.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointTransferRequestMessage {
	private Long senderUserId;
	private Long receiverUserId;
	private int amount;
	private String reason;
}

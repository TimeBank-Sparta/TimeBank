package com.timebank.helpservice.help_trading.application.dto.request;

import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record PointTransferRequestMessage(
	Long senderUserId,
	Long receiverUserId,
	int amount,
	String reason
) {
	public static PointTransferRequestMessage from(HelpTrading helpTrading) {
		return PointTransferRequestMessage.builder()
			.senderUserId(helpTrading.getRequesterId())
			.receiverUserId(helpTrading.getHelperId())
			.amount(helpTrading.getActualPoints())
			.reason("거래완료")
			.build();
	}
}
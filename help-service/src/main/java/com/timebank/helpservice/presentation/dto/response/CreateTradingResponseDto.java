package com.timebank.helpservice.presentation.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.TradeStatus;
import com.timebank.helpservice.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record CreateTradingResponseDto(
	Long requesterId,
	Long helperId,
	LocalDateTime startedAt,
	LocalDateTime finishedAt,
	int actualPoints,
	boolean requesterApproved,
	boolean helperApproved,
	TradeStatus tradeStatus
) {
	public static CreateTradingResponseDto of(HelpTrading helpTrading) {
		return CreateTradingResponseDto.builder()
			.requesterId(helpTrading.getRequesterId())
			.helperId(helpTrading.getHelperId())
			.startedAt(helpTrading.getStartedAt())
			.finishedAt(helpTrading.getFinishedAt())
			.actualPoints(helpTrading.getActualPoints())
			.requesterApproved(helpTrading.isRequesterApproved())
			.helperApproved(helpTrading.isHelperApproved())
			.tradeStatus(helpTrading.getTradeStatus())
			.build();
	}
}

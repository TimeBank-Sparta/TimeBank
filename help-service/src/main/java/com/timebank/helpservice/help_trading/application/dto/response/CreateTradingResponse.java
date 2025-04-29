package com.timebank.helpservice.help_trading.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record CreateTradingResponse(
	Long requesterId,
	Long helperId,
	LocalDateTime startedAt,
	LocalDateTime finishedAt,
	int actualPoints,
	boolean requesterApproved,
	boolean helperApproved,
	TradeStatus tradeStatus
) {
	public static CreateTradingResponse from(HelpTrading helpTrading) {
		return CreateTradingResponse.builder()
			.requesterId(helpTrading.getRequesterId())
			.helperId(helpTrading.getHelperId())
			.startedAt(helpTrading.getStartedAt())
			.finishedAt(helpTrading.getFinishedAt())
			.actualPoints(helpTrading.getActualPoints())
			.tradeStatus(helpTrading.getTradeStatus())
			.build();
	}
}

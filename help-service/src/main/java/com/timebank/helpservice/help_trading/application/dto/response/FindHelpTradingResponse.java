package com.timebank.helpservice.help_trading.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record FindHelpTradingResponse(
	Long helpTradingId,
	Long requesterId,
	Long helperId,
	LocalDateTime startedAt,
	LocalDateTime finishedAt,
	int actualPoints,
	TradeStatus tradeStatus
) {
	public static FindHelpTradingResponse from(HelpTrading helpTrading) {
		return FindHelpTradingResponse.builder()
			.helpTradingId(helpTrading.getId())
			.requesterId(helpTrading.getRequesterId())
			.helperId(helpTrading.getHelperId())
			.startedAt(helpTrading.getStartedAt())
			.finishedAt(helpTrading.getFinishedAt())
			.actualPoints(helpTrading.getActualPoints())
			.build();
	}
}

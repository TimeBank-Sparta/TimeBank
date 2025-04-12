package com.timebank.helpservice.help_trading.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record FindHelpTradingResponse(
	Long requesterId,
	Long helperId,
	LocalDateTime startedAt,
	LocalDateTime finishedAt,
	int actualPoints,
	boolean requesterApproved,
	boolean helperApproved,
	TradeStatus tradeStatus
) {
	public static FindHelpTradingResponse from(HelpTrading helpTrading) {
		return FindHelpTradingResponse.builder()
			.requesterId(helpTrading.getRequesterId())
			.helperId(helpTrading.getHelperId())
			.startedAt(helpTrading.getStartedAt())
			.finishedAt(helpTrading.getFinishedAt())
			.actualPoints(helpTrading.getActualPoints())
			.requesterApproved(helpTrading.isRequesterApproved())
			.helperApproved(helpTrading.isHelperApproved())
			.build();
	}
}

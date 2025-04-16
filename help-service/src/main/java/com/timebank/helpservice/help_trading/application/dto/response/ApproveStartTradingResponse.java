package com.timebank.helpservice.help_trading.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record ApproveStartTradingResponse(
	TradeStatus tradeStatus,
	LocalDateTime startedAt
) {
	public static ApproveStartTradingResponse from(HelpTrading helpTrading) {
		return ApproveStartTradingResponse.builder()
			.tradeStatus(helpTrading.getTradeStatus())
			.startedAt(helpTrading.getStartedAt())
			.build();
	}
}

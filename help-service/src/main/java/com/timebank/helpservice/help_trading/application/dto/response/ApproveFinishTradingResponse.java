package com.timebank.helpservice.help_trading.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record ApproveFinishTradingResponse(
	TradeStatus tradeStatus,
	LocalDateTime finishedAt
) {
	public static ApproveFinishTradingResponse from(HelpTrading helpTrading) {
		return ApproveFinishTradingResponse.builder()
			.tradeStatus(helpTrading.getTradeStatus())
			.finishedAt(helpTrading.getFinishedAt())
			.build();
	}
}

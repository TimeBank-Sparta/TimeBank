package com.timebank.helpservice.help_trading.application.dto.response;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record CancelTradingResponse(
	Long helpTradingId,
	TradeStatus tradeStatus
) {
	public static CancelTradingResponse from(HelpTrading helpTrading) {
		return CancelTradingResponse.builder()
			.helpTradingId(helpTrading.getId())
			.tradeStatus(helpTrading.getTradeStatus())
			.build();
	}
}

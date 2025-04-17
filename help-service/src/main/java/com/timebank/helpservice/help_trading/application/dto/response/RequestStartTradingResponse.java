package com.timebank.helpservice.help_trading.application.dto.response;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record RequestStartTradingResponse(
	TradeStatus tradeStatus
) {
	public static RequestStartTradingResponse from(HelpTrading helpTrading) {
		return RequestStartTradingResponse.builder()
			.tradeStatus(helpTrading.getTradeStatus())
			.build();
	}
}

package com.timebank.helpservice.help_trading.application.dto.response;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

import lombok.Builder;

@Builder
public record RequestFinishTradingResponse(
	TradeStatus tradeStatus
) {
	public static RequestFinishTradingResponse from(HelpTrading helpTrading) {
		return RequestFinishTradingResponse.builder()
			.tradeStatus(helpTrading.getTradeStatus())
			.build();
	}
}

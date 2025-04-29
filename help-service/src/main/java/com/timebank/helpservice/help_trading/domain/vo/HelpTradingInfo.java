package com.timebank.helpservice.help_trading.domain.vo;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;

import lombok.Builder;

@Builder
public record HelpTradingInfo(
	Long helpRequestId,
	Long requesterId,
	Long helperId,
	LocalDateTime startedAt,
	int actualPoints,
	TradeStatus tradeStatus
) {

}

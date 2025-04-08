package com.timebank.helpservice.domain.vo;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.TradeStatus;

import lombok.Builder;

@Builder
public record HelpTradingInfo(
	Long requesterId,
	Long helperId,
	LocalDateTime startedAt,
	int actualPoints,
	boolean requesterApproved,
	boolean helperApproved,
	TradeStatus tradeStatus
) {

}

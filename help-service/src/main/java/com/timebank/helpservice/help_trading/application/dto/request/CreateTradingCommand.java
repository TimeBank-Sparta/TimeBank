package com.timebank.helpservice.help_trading.application.dto.request;

import com.timebank.helpservice.help_trading.domain.vo.HelpTradingInfo;

import lombok.Builder;

@Builder
public record CreateTradingCommand(
	Long helpRequestId,
	Long requesterId,
	Long helperId,
	int actualPoints
) {
	public HelpTradingInfo toHelpTradingInfo() {
		return HelpTradingInfo.builder()
			.helpRequestId(helpRequestId)
			.requesterId(requesterId)
			.helperId(helperId)
			.actualPoints(actualPoints)
			.build();
	}
}

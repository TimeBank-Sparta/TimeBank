package com.timebank.helpservice.help_trading.presentation.dto.request;

import com.timebank.helpservice.help_trading.application.dto.request.CreateTradingCommand;

public record CreateTradingRequest(
	Long helpRequestId,
	Long requesterId,
	Long helperId,
	int actualPoints
) {
	public CreateTradingCommand toCommand() {
		return CreateTradingCommand.builder()
			.helpRequestId(helpRequestId)
			.requesterId(requesterId)
			.helperId(helperId)
			.actualPoints(actualPoints)
			.build();
	}
}

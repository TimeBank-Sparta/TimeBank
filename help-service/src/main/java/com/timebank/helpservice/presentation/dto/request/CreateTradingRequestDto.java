package com.timebank.helpservice.presentation.dto.request;

public record CreateTradingRequestDto(
	Long helpRequestId,
	Long requesterId,
	Long helperId,
	int actualPoints
) {
}

package com.timebank.helpservice.helper.application.dto.request;

public record HelpRequestFeignDto(
	Long requesterId,
	int requestedPoint,
	int recruitmentCount
) {
}

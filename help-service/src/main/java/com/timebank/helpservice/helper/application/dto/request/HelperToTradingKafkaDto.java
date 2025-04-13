package com.timebank.helpservice.helper.application.dto.request;

import lombok.Builder;

@Builder
public record HelperToTradingKafkaDto(
	Long helpRequestId,
	Long helperId,
	Long requesterId,
	int requestedPoint
) {
	public static HelperToTradingKafkaDto of(
		Long helpRequestId,
		Long helperId,
		Long requesterId,
		int requestedPoint
	) {
		return HelperToTradingKafkaDto.builder()
			.helpRequestId(helpRequestId)
			.helperId(helperId)
			.requesterId(requesterId)
			.requestedPoint(requestedPoint)
			.build();
	}
}

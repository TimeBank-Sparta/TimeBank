package com.timebank.helpservice.help_request.application.dto.request;

import lombok.Builder;

@Builder
public record HoldPointRequestDto(
	Long userId,
	int amount
) {
	public static HoldPointRequestDto of(Long userId, int amount) {
		return HoldPointRequestDto.builder()
			.userId(userId)
			.amount(amount)
			.build();
	}
}

package com.timebank.helpservice.help_trading.application.dto.response;

import com.timebank.helpservice.help_trading.domain.vo.HelpTradingInfo;

import lombok.Builder;

@Builder
public record FromHelperKafkaDto(
	Long helpRequestId,
	Long helperId,
	Long requesterId,
	int requestedPoint
) {
	public HelpTradingInfo toHelpTradingInfo() {
		return HelpTradingInfo.builder()
			.helpRequestId(helpRequestId)
			.requesterId(requesterId)
			.helperId(helperId)
			.actualPoints(requestedPoint)
			.build();
	}
}
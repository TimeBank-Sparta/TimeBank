package com.timebank.helpservice.help_request.application.dto.request;

import lombok.Builder;

@Builder
public record HelpRequestToHelperKafkaDto(
	Long helpRequestId
) {
	public static HelpRequestToHelperKafkaDto of(Long helpRequestId) {
		return HelpRequestToHelperKafkaDto.builder()
			.helpRequestId(helpRequestId)
			.build();
	}
}

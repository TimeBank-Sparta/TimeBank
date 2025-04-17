package com.timebank.helpservice.helper.presentation.dto.request;

import com.timebank.helpservice.helper.application.dto.request.CreateHelperCommand;

public record CreateHelperRequest(
	Long helpRequestId
) {
	public CreateHelperCommand toCommand() {
		return CreateHelperCommand.builder()
			.helpRequestId(helpRequestId)
			.build();
	}
}

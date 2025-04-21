package com.timebank.helpservice.helper.application.dto.response;

import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record AcceptHelperResponse(
	Long helpRequestId
) {
	public static AcceptHelperResponse from(Helper helper) {
		return AcceptHelperResponse.builder()
			.helpRequestId(helper.getHelpRequestId())
			.build();
	}
}

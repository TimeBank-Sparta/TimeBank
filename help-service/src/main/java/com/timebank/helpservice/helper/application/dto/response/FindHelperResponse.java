package com.timebank.helpservice.helper.application.dto.response;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record FindHelperResponse(
	String username,
	ApplicantStatus status
) {
	public static FindHelperResponse of(GetUserInfoFeignResponse response, Helper helper) {
		return FindHelperResponse.builder()
			.username(response.username())
			.status(helper.getApplicantStatus())
			.build();

	}
}

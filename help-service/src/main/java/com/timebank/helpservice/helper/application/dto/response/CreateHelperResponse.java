package com.timebank.helpservice.helper.application.dto.response;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record CreateHelperResponse(
	Long userId,
	ApplicantStatus applicantStatus
) {
	public static CreateHelperResponse from(Helper helper) {
		return CreateHelperResponse.builder()
			.userId(helper.getUserId())
			.applicantStatus(helper.getApplicantStatus())
			.build();
	}
}

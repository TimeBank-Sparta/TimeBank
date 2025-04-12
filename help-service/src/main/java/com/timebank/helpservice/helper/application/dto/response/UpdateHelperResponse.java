package com.timebank.helpservice.helper.application.dto.response;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record UpdateHelperResponse(
	Long helperId,
	Long userId,
	ApplicantStatus applicantStatus
) {
	public static UpdateHelperResponse from(Helper helper) {
		return UpdateHelperResponse.builder()
			.helperId(helper.getId())
			.userId(helper.getUserId())
			.applicantStatus(helper.getApplicantStatus())
			.build();
	}
}

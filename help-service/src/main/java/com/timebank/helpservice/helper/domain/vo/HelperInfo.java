package com.timebank.helpservice.helper.domain.vo;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record HelperInfo(
	Long helpRequestId,
	Long userId,
	ApplicantStatus applicantStatus
) {
	public static HelperInfo from(Helper helper) {
		return HelperInfo.builder()
			.helpRequestId(helper.getHelpRequestId())
			.userId(helper.getUserId())
			.applicantStatus(helper.getApplicantStatus())
			.build();
	}
}

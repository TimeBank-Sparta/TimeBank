package com.timebank.helpservice.helper.domain.vo;

import com.timebank.helpservice.helper.domain.ApplicantStatus;

import lombok.Builder;

@Builder
public record HelperInfo(
	Long helpRequestId,
	Long userId,
	ApplicantStatus applicantStatus
) {
}

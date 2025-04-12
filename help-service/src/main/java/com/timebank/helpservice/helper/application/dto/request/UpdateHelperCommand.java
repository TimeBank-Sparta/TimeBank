package com.timebank.helpservice.helper.application.dto.request;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.vo.HelperInfo;

import lombok.Builder;

@Builder
public record UpdateHelperCommand(
	Long helpRequestId,
	ApplicantStatus applicantStatus
) {
	public HelperInfo toHelperInfo() {
		return HelperInfo.builder()
			.helpRequestId(helpRequestId)
			.applicantStatus(applicantStatus)
			.build();
	}
}

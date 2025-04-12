package com.timebank.helpservice.helper.presentation.dto.request;

import com.timebank.helpservice.helper.application.dto.request.UpdateHelperCommand;
import com.timebank.helpservice.helper.domain.ApplicantStatus;

public record UpdateHelperRequest(
	Long helpRequestId,
	ApplicantStatus applicantStatus
) {
	public UpdateHelperCommand toCommand() {
		return UpdateHelperCommand.builder()
			.helpRequestId(helpRequestId)
			.applicantStatus(applicantStatus)
			.build();
	}
}

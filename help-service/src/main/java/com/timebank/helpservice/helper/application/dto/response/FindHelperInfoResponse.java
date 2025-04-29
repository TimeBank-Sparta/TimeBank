package com.timebank.helpservice.helper.application.dto.response;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;

import lombok.Builder;

@Builder
public record FindHelperInfoResponse(
	String nickname,
	Double trustScore,
	ApplicantStatus status
) {
	public static FindHelperInfoResponse of(GetUserInfoFeignResponse response, Helper helper) {
		return FindHelperInfoResponse.builder()
			.nickname(response.nickname())
			.trustScore(response.trustScore())
			.status(helper.getApplicantStatus())
			.build();

	}
}

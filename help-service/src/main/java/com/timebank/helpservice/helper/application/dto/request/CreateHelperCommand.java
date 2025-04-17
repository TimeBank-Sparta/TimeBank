package com.timebank.helpservice.helper.application.dto.request;

import com.timebank.helpservice.helper.domain.vo.HelperInfo;

import lombok.Builder;

@Builder
public record CreateHelperCommand(
	Long helpRequestId,
	Long requesterId
) {
	public HelperInfo toHelperInfoWithUserId(Long userId) {
		return HelperInfo.builder()
			.helpRequestId(helpRequestId)
			.userId(userId)
			.build();
	}
}

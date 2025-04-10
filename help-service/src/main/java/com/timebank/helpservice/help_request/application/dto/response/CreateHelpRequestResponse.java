package com.timebank.helpservice.help_request.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.domain.PostStatus;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;

import lombok.Builder;

@Builder
public record CreateHelpRequestResponse(
	Long helpRequestId,
	String title,
	String content,
	String address,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus
) {
	public static CreateHelpRequestResponse from(HelpRequest helpRequest) {
		return CreateHelpRequestResponse.builder()
			.helpRequestId(helpRequest.getId())
			.title(helpRequest.getTitle())
			.content(helpRequest.getContent())
			.address(helpRequest.getAddress())
			.scheduledAt(helpRequest.getScheduledAt())
			.requiredTime(helpRequest.getRequiredTime())
			.requestedPoint(helpRequest.getRequestedPoint())
			.recruitmentCount(helpRequest.getRecruitmentCount())
			.postStatus(helpRequest.getPostStatus())
			.build();
	}
}

package com.timebank.helpservice.help_request.application.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.domain.PostStatus;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestLocation;

import lombok.Builder;

@Builder
public record CreateHelpRequestResponse(
	Long helpRequestId,
	String title,
	String content,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus,
	HelpRequestLocation location
) {
	public static CreateHelpRequestResponse from(HelpRequest helpRequest) {
		return CreateHelpRequestResponse.builder()
			.helpRequestId(helpRequest.getId())
			.title(helpRequest.getTitle())
			.content(helpRequest.getContent())
			.scheduledAt(helpRequest.getScheduledAt())
			.requiredTime(helpRequest.getRequiredTime())
			.requestedPoint(helpRequest.getRequestedPoint())
			.recruitmentCount(helpRequest.getRecruitmentCount())
			.postStatus(helpRequest.getPostStatus())
			.location(helpRequest.getLocation())
			.build();
	}
}

package com.timebank.helpservice.presentation.dto.response;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.PostStatus;
import com.timebank.helpservice.domain.model.HelpRequest;

import lombok.Builder;

@Builder
public record CreateHelpResponseDto(
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
	public static CreateHelpResponseDto from(HelpRequest helpRequest) {
		return CreateHelpResponseDto.builder()
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

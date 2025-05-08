package com.timebank.helpservice.help_request.application.dto.request;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.domain.PostStatus;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestInfo;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestLocation;

import lombok.Builder;

@Builder
public record UpdateHelpRequestCommand(
	String title,
	String content,
	String address,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus
) {
	public HelpRequestInfo toHelpRequestInfoWithLocation(HelpRequestLocation location) {
		return HelpRequestInfo.builder()
			.title(title)
			.content(content)
			.address(address)
			.scheduledAt(scheduledAt)
			.requiredTime(requiredTime)
			.requestedPoint(requestedPoint)
			.recruitmentCount(recruitmentCount)
			.postStatus(postStatus)
			.location(location)
			.build();
	}
}

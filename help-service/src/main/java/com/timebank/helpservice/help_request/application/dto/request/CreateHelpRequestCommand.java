package com.timebank.helpservice.help_request.application.dto.request;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.domain.PostStatus;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestInfo;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestLocation;

import lombok.Builder;

@Builder
public record CreateHelpRequestCommand(
	String title,
	String content,
	String address,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus
) {
	public HelpRequestInfo toHelpRequestInfoWithUserIdAndLocation(Long userId, HelpRequestLocation location) {
		return HelpRequestInfo.builder()
			.requesterId(userId)
			.title(title)
			.content(content)
			.address(address)
			.scheduledAt(scheduledAt)
			.requiredTime(requiredTime)
			.requestedPoint(requestedPoint)
			.recruitmentCount(recruitmentCount)
			.location(location)
			.build();
	}
}

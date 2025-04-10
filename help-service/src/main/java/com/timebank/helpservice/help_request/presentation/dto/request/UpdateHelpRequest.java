package com.timebank.helpservice.help_request.presentation.dto.request;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.application.dto.request.UpdateHelpRequestCommand;
import com.timebank.helpservice.help_request.domain.PostStatus;

public record UpdateHelpRequest(
	String title,
	String content,
	String address,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus
) {
	public UpdateHelpRequestCommand toCommand() {
		return UpdateHelpRequestCommand.builder()
			.title(title)
			.content(content)
			.address(address)
			.scheduledAt(scheduledAt)
			.requiredTime(requiredTime)
			.requestedPoint(requestedPoint)
			.recruitmentCount(recruitmentCount)
			.postStatus(postStatus)
			.build();
	}
}

package com.timebank.helpservice.help_request.presentation.dto.request;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.application.dto.request.CreateHelpRequestCommand;
import com.timebank.helpservice.help_request.domain.PostStatus;

public record CreateHelpRequest(
	String title,
	String content,
	String address,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus
) {
	public CreateHelpRequestCommand toCommand() {
		return CreateHelpRequestCommand.builder()
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

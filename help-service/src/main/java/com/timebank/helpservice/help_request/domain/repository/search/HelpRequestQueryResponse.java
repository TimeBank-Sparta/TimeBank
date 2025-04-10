package com.timebank.helpservice.help_request.domain.repository.search;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.domain.PostStatus;

import lombok.Builder;

@Builder
public record HelpRequestQueryResponse(
	Long id,
	String title,
	String content,
	String address,
	LocalDateTime scheduledAt,
	int requiredTime,
	int requestedPoint,
	int recruitmentCount,
	PostStatus postStatus
) {
}

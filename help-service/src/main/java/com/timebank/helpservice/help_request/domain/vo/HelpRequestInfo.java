package com.timebank.helpservice.help_request.domain.vo;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_request.domain.PostStatus;

import lombok.Builder;

@Builder
public record HelpRequestInfo(
	Long requesterId,
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

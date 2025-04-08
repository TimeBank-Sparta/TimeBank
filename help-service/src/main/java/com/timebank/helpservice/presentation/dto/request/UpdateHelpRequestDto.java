package com.timebank.helpservice.presentation.dto.request;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.PostStatus;

public record UpdateHelpRequestDto(
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
}

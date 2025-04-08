package com.timebank.helpservice.presentation.dto.request;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.PostStatus;

import lombok.Builder;

@Builder
public record CreateHelpRequestDto(
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

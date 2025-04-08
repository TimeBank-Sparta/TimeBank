package com.timebank.helpservice.domain.vo;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.PostStatus;

import lombok.Builder;

@Builder
public record HelpRequestInfo(
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

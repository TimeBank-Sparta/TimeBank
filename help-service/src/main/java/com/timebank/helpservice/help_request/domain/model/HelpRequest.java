package com.timebank.helpservice.help_request.domain.model;

import java.time.LocalDateTime;

import com.timebank.common.domain.Timestamped;
import com.timebank.helpservice.help_request.domain.PostStatus;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestInfo;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestLocation;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestMetrics;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(
	name = "p_help_request",
	indexes = {
		@Index(name = "idx_requester_id", columnList = "requesterId"),
		@Index(name = "idx_title", columnList = "title"),
		@Index(name = "idx_scheduled_at", columnList = "scheduledAt")
	}
)
public class HelpRequest extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "help_request_id")
	private Long id;

	private Long requesterId;

	private String title;

	private String content;

	private int requiredTime;

	private LocalDateTime scheduledAt;

	private int requestedPoint;

	private int recruitmentCount;

	@Enumerated(EnumType.STRING)
	private PostStatus postStatus;

	@Embedded
	private HelpRequestMetrics postMetric;

	@Embedded
	private HelpRequestLocation location;

	@Builder(builderMethodName = "helpRequestOnlyBuilder")
	public HelpRequest(
		Long requesterId,
		String title,
		String content,
		LocalDateTime scheduledAt,
		int requiredTime,
		int requestedPoint,
		int recruitmentCount,
		PostStatus postStatus,
		HelpRequestLocation location
	) {
		this.requesterId = requesterId;
		this.title = title;
		this.content = content;
		this.scheduledAt = scheduledAt;
		this.requiredTime = requiredTime;
		this.requestedPoint = requestedPoint;
		this.recruitmentCount = recruitmentCount;
		this.postStatus = postStatus;
		this.location = location;
	}

	public static HelpRequest createFrom(HelpRequestInfo helpRequestInfo) {
		return HelpRequest.helpRequestOnlyBuilder()
			.requesterId(helpRequestInfo.requesterId())
			.title(helpRequestInfo.title())
			.content(helpRequestInfo.content())
			.scheduledAt(helpRequestInfo.scheduledAt())
			.requiredTime(helpRequestInfo.requiredTime())
			.requestedPoint(helpRequestInfo.requestedPoint())
			.recruitmentCount(helpRequestInfo.recruitmentCount())
			.postStatus(PostStatus.IN_PROGRESS)
			.location(helpRequestInfo.location())
			.build();
	}

	public void update(HelpRequestInfo helpRequestInfo) {
		this.title = helpRequestInfo.title();
		this.content = helpRequestInfo.content();
		this.scheduledAt = helpRequestInfo.scheduledAt();
		this.requiredTime = helpRequestInfo.requiredTime();
		this.requestedPoint = helpRequestInfo.requestedPoint();
		this.recruitmentCount = helpRequestInfo.recruitmentCount();
		this.postStatus = helpRequestInfo.postStatus();
		this.location = helpRequestInfo.location();
	}

	public void completePostStatus() {
		this.postStatus = PostStatus.COMPLETED;
	}
}

package com.timebank.helpservice.help_request.domain.model;

import java.time.LocalDateTime;

import com.timebank.common.domain.Timestamped;
import com.timebank.helpservice.help_request.domain.PostStatus;
import com.timebank.helpservice.help_request.domain.vo.HelpRequestInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "p_help_request")
public class HelpRequest extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "help_request_id")
	private Long id;

	private Long requesterId;

	private String title;

	private String content;

	private String address;

	private int requiredTime;

	private LocalDateTime scheduledAt;

	private int requestedPoint;

	private int recruitmentCount;

	@Enumerated(EnumType.STRING)
	private PostStatus postStatus;

	@Builder(builderMethodName = "helpRequestOnlyBuilder")
	public HelpRequest(
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
		this.requesterId = requesterId;
		this.title = title;
		this.content = content;
		this.address = address;
		this.scheduledAt = scheduledAt;
		this.requiredTime = requiredTime;
		this.requestedPoint = requestedPoint;
		this.recruitmentCount = recruitmentCount;
		this.postStatus = postStatus;
	}

	public static HelpRequest createFrom(HelpRequestInfo helpRequestInfo) {
		return HelpRequest.helpRequestOnlyBuilder()
			.requesterId(helpRequestInfo.requesterId())
			.title(helpRequestInfo.title())
			.content(helpRequestInfo.content())
			.address(helpRequestInfo.address())
			.scheduledAt(helpRequestInfo.scheduledAt())
			.requiredTime(helpRequestInfo.requiredTime())
			.requestedPoint(helpRequestInfo.requestedPoint())
			.recruitmentCount(helpRequestInfo.recruitmentCount())
			.postStatus(PostStatus.IN_PROGRESS)
			.build();
	}

	public void update(HelpRequestInfo helpRequestInfo) {
		this.title = helpRequestInfo.title();
		this.content = helpRequestInfo.content();
		this.address = helpRequestInfo.address();
		this.scheduledAt = helpRequestInfo.scheduledAt();
		this.requiredTime = helpRequestInfo.requiredTime();
		this.requestedPoint = helpRequestInfo.requestedPoint();
		this.recruitmentCount = helpRequestInfo.recruitmentCount();
		this.postStatus = helpRequestInfo.postStatus();
	}

	public void completePostStatus() {
		this.postStatus = PostStatus.COMPLETED;
	}
}

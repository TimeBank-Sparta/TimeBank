package com.timebank.helpservice.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.timebank.helpservice.domain.PostStatus;
import com.timebank.helpservice.domain.vo.HelpRequestInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class HelpRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "help_request_id")
	private Long id;

	private String title;

	private String content;

	private String address;

	private int requiredTime;

	private LocalDateTime scheduledAt;

	private int requestedPoint;

	private int recruitmentCount;

	@Enumerated(EnumType.STRING)
	private PostStatus postStatus;

	@OneToMany(mappedBy = "helpRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Helper> helperSet = new HashSet<>();

	@OneToMany(mappedBy = "helpRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HelpTrading> helpTradingList = new ArrayList<>();

	@Builder(builderMethodName = "helpRequestOnlyBuilder")
	public HelpRequest(
		String title,
		String content,
		String address,
		LocalDateTime scheduledAt,
		int requiredTime,
		int requestedPoint,
		int recruitmentCount,
		PostStatus postStatus
	) {
		this.title = title;
		this.content = content;
		this.address = address;
		this.scheduledAt = scheduledAt;
		this.requiredTime = requiredTime;
		this.requestedPoint = requestedPoint;
		this.recruitmentCount = recruitmentCount;
		this.postStatus = postStatus;
	}

	public static HelpRequest createOf(HelpRequestInfo helpRequestInfo) {
		return HelpRequest.helpRequestOnlyBuilder()
			.title(helpRequestInfo.title())
			.content(helpRequestInfo.content())
			.address(helpRequestInfo.address())
			.scheduledAt(helpRequestInfo.scheduledAt())
			.requiredTime(helpRequestInfo.requiredTime())
			.requestedPoint(helpRequestInfo.requestedPoint())
			.recruitmentCount(helpRequestInfo.recruitmentCount())
			.postStatus(PostStatus.OPEN)
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

	public void addHelper(Helper helper) {
		this.helperSet.add(helper);
		helper.addHelpRequest(this);
	}

	public void addHelpTrading(HelpTrading helpTrading) {
		this.helpTradingList.add(helpTrading);
		helpTrading.addHelpRequest(this);
	}
}

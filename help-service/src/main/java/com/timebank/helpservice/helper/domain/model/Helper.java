package com.timebank.helpservice.helper.domain.model;

import com.timebank.common.domain.Timestamped;
import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.vo.HelperInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_helper")
public class Helper extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long helpRequestId;

	@Column(nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	private ApplicantStatus applicantStatus;

	@Builder
	public Helper(Long helpRequestId, Long userId, ApplicantStatus applicantStatus) {
		this.helpRequestId = helpRequestId;
		this.userId = userId;
		this.applicantStatus = applicantStatus;
	}

	public static Helper createFrom(HelperInfo helpInfo) {
		return Helper.builder()
			.userId(helpInfo.userId())
			.helpRequestId(helpInfo.helpRequestId())
			.applicantStatus(ApplicantStatus.SUPPORTED)
			.build();
	}

	public void acceptHelperStatus() {
		this.applicantStatus = ApplicantStatus.ACCEPTED;
	}
}

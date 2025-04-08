package com.timebank.helpservice.domain.model;

import com.timebank.helpservice.domain.ApplicantStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "userId")
@Entity
@Table(name = "p_helper")
public class Helper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "help_request_id")
	private HelpRequest helpRequest;

	@Column(nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	private ApplicantStatus applicantStatus;

	@Builder
	public Helper(Long userId, ApplicantStatus applicantStatus) {
		this.userId = userId;
		this.applicantStatus = applicantStatus;
	}

	public void addHelpRequest(HelpRequest helpRequest) {
		this.helpRequest = helpRequest;
	}

}

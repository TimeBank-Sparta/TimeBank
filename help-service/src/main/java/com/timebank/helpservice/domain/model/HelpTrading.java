package com.timebank.helpservice.domain.model;

import java.time.LocalDateTime;

import com.timebank.helpservice.domain.TradeStatus;
import com.timebank.helpservice.domain.vo.HelpTradingInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_help_trading")
public class HelpTrading {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long requesterId;

	private Long helperId;

	private LocalDateTime startedAt;

	private LocalDateTime finishedAt;

	private int actualPoints;

	private boolean requesterApproved;

	private boolean helperApproved;

	private TradeStatus tradeStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "help_request_id")
	private HelpRequest helpRequest;

	@Builder(builderMethodName = "startTradingBuilder")
	public HelpTrading(
		Long requesterId,
		Long helperId,
		LocalDateTime startedAt,
		int actualPoints,
		boolean requesterApproved,
		boolean helperApproved,
		TradeStatus tradeStatus
	) {
		this.requesterId = requesterId;
		this.helperId = helperId;
		this.startedAt = startedAt;
		this.actualPoints = actualPoints;
		this.requesterApproved = requesterApproved;
		this.helperApproved = helperApproved;
		this.tradeStatus = tradeStatus;
	}

	public static HelpTrading createOf(HelpTradingInfo helpTradingInfo) {
		return HelpTrading.startTradingBuilder()
			.requesterId(helpTradingInfo.requesterId())
			.helperId(helpTradingInfo.helperId())
			.startedAt(LocalDateTime.now())
			.actualPoints(helpTradingInfo.actualPoints())
			.requesterApproved(false)
			.helperApproved(false)
			.tradeStatus(TradeStatus.IN_PROGRESS)
			.build();
	}

	public void addHelpRequest(HelpRequest helpRequest) {
		this.helpRequest = helpRequest;
	}
}

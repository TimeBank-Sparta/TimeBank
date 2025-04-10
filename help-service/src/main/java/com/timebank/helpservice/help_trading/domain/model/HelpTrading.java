package com.timebank.helpservice.help_trading.domain.model;

import java.time.LocalDateTime;

import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.vo.HelpTradingInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	@Column(name = "help_request_id")
	private Long helpRequestId;

	@Builder(builderMethodName = "startTradingBuilder")
	public HelpTrading(
		Long helpRequestId,
		Long requesterId,
		Long helperId,
		LocalDateTime startedAt,
		int actualPoints,
		boolean requesterApproved,
		boolean helperApproved,
		TradeStatus tradeStatus
	) {
		this.helpRequestId = helpRequestId;
		this.requesterId = requesterId;
		this.helperId = helperId;
		this.startedAt = startedAt;
		this.actualPoints = actualPoints;
		this.requesterApproved = requesterApproved;
		this.helperApproved = helperApproved;
		this.tradeStatus = tradeStatus;
	}

	public static HelpTrading createFrom(HelpTradingInfo helpTradingInfo) {
		return HelpTrading.startTradingBuilder()
			.helpRequestId(helpTradingInfo.helpRequestId())
			.requesterId(helpTradingInfo.requesterId())
			.helperId(helpTradingInfo.helperId())
			.startedAt(LocalDateTime.now())
			.actualPoints(helpTradingInfo.actualPoints())
			.requesterApproved(false)
			.helperApproved(false)
			.tradeStatus(TradeStatus.IN_PROGRESS)
			.build();
	}
}

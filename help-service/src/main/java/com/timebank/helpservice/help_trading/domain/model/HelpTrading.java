package com.timebank.helpservice.help_trading.domain.model;

import java.time.LocalDateTime;

import com.timebank.common.domain.Timestamped;
import com.timebank.helpservice.help_trading.domain.TradeStatus;
import com.timebank.helpservice.help_trading.domain.vo.HelpTradingInfo;

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
@Table(name = "p_help_trading")
public class HelpTrading extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long requesterId;

	private Long helperId;

	private LocalDateTime startedAt;

	private LocalDateTime finishedAt;

	private int actualPoints;

	@Enumerated(EnumType.STRING)
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
		TradeStatus tradeStatus
	) {
		this.helpRequestId = helpRequestId;
		this.requesterId = requesterId;
		this.helperId = helperId;
		this.startedAt = startedAt;
		this.actualPoints = actualPoints;
		this.tradeStatus = tradeStatus;
	}

	public static HelpTrading createFrom(HelpTradingInfo helpTradingInfo) {
		return HelpTrading.startTradingBuilder()
			.helpRequestId(helpTradingInfo.helpRequestId())
			.requesterId(helpTradingInfo.requesterId())
			.helperId(helpTradingInfo.helperId())
			.startedAt(LocalDateTime.now())
			.actualPoints(helpTradingInfo.actualPoints())
			.tradeStatus(TradeStatus.CREATED)
			.build();
	}

	public HelpTrading updateStatus(TradeStatus tradeStatus) {
		this.tradeStatus = tradeStatus;
		return this;
	}

	public HelpTrading updateStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
		return this;
	}

	public HelpTrading updateFinishedAt(LocalDateTime finishedAt) {
		this.finishedAt = finishedAt;
		return this;
	}
}

package com.timebank.helpservice.help_trading.application.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.common.application.exception.CustomNotFoundException;
import com.timebank.helpservice.help_trading.application.dto.request.CreateTradingCommand;
import com.timebank.helpservice.help_trading.application.dto.request.PointTransferRequestMessage;
import com.timebank.helpservice.help_trading.application.dto.response.ApproveFinishTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.ApproveStartTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.CreateTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FindHelpTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FromHelperKafkaDto;
import com.timebank.helpservice.help_trading.application.dto.response.RequestFinishTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.RequestStartTradingResponse;
import com.timebank.helpservice.help_trading.domain.UserRole;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;
import com.timebank.helpservice.help_trading.domain.repository.HelpTradingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpTradingService {
	private final HelpTradingRepository helpTradingRepository;
	private final HelpTradingEventProducer eventProducer;

	@Transactional
	public CreateTradingResponse createHelpTrading(CreateTradingCommand command) {
		//TODO 게시글 존재여부 확인
		return CreateTradingResponse.from(
			helpTradingRepository.save(HelpTrading.createFrom(command.toHelpTradingInfo())));
	}

	@Transactional
	public void createHelpTradingFromKafka(FromHelperKafkaDto command) {
		helpTradingRepository.save(HelpTrading.createFrom(command.toHelpTradingInfo()));
	}

	@Transactional(readOnly = true)
	public Page<FindHelpTradingResponse> findByHelpRequestId(
		Long helpRequestId, Pageable pageable
	) {
		return helpTradingRepository.findByHelpRequestId(helpRequestId, pageable)
			.map(FindHelpTradingResponse::from);
	}

	@Transactional
	public void delete(Long helpRequestId) {
		HelpTrading helpTrading = helpTradingRepository.findById(helpRequestId).orElseThrow(() ->
			new CustomNotFoundException("거래내역이 없습니다."));
	}

	@Transactional
	public RequestStartTradingResponse requestStartTrading(Long helpTradingId, Long userId) {
		return RequestStartTradingResponse.from(updateTradeStatus(helpTradingId, userId));
	}

	@Transactional
	public ApproveStartTradingResponse approveStartTrading(Long helpTradingId, Long userId) {
		return ApproveStartTradingResponse.from(updateTradeStatus(helpTradingId, userId)
			.updateStartedAt(LocalDateTime.now()));
	}

	@Transactional
	public RequestFinishTradingResponse requestFinishTrading(Long helpTradingId, Long userId) {
		return RequestFinishTradingResponse.from(updateTradeStatus(helpTradingId, userId));
	}

	@Transactional
	public ApproveFinishTradingResponse approveFinishTrading(Long helpTradingId, Long userId) {
		HelpTrading helpTrading = updateTradeStatus(helpTradingId, userId)
			.updateFinishedAt(LocalDateTime.now());

		eventProducer.sendToPoints(PointTransferRequestMessage.from(helpTrading));

		return ApproveFinishTradingResponse.from(helpTrading);
	}

	public HelpTrading updateTradeStatus(Long helpTradingId, Long userId) {
		HelpTrading helpTrading = getHelpTradingOrThrow(helpTradingId);
		UserRole role = UserRole.NONE;

		if (helpTrading.getRequesterId().equals(userId)) {
			role = UserRole.HELPER;
		}
		if (helpTrading.getHelperId().equals(userId)) {
			role = UserRole.REQUESTER;
		}

		return helpTrading.updateStatus(helpTrading.getTradeStatus().next(role));
	}

	public HelpTrading getHelpTradingOrThrow(Long helpTradingId) {
		return helpTradingRepository.findById(helpTradingId).orElseThrow(() ->
			new CustomNotFoundException("거래내역이 없습니다."));
	}
}

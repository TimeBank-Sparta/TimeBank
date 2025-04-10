package com.timebank.helpservice.help_trading.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.helpservice.help_trading.application.dto.request.CreateTradingCommand;
import com.timebank.helpservice.help_trading.application.dto.response.CreateTradingResponse;
import com.timebank.helpservice.help_trading.application.dto.response.FindHelpTradingResponse;
import com.timebank.helpservice.help_trading.domain.model.HelpTrading;
import com.timebank.helpservice.help_trading.domain.repository.HelpTradingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HelpTradingService {
	private final HelpTradingRepository helpTradingRepository;

	@Transactional
	public CreateTradingResponse createHelpTrading(CreateTradingCommand command) {
		//TODO 게시글 존재여부 확인
		return CreateTradingResponse.from(
			helpTradingRepository.save(HelpTrading.createFrom(command.toHelpTradingInfo())));
	}

	@Transactional(readOnly = true)
	public Page<FindHelpTradingResponse> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		Page<HelpTrading> helpTrading = helpTradingRepository.findByHelpRequestId(helpRequestId, pageable)
			.orElseThrow(() -> new IllegalArgumentException("Help Trading not found"));

		return new PageImpl<>(helpTrading.getContent().stream()
			.map(FindHelpTradingResponse::from).toList(),
			pageable, helpTrading.getTotalElements());
	}

	public void delete(Long helpRequestId) {
		HelpTrading helpTrading = helpTradingRepository.findById(helpRequestId).orElseThrow(() ->
			new IllegalArgumentException("Help request not found"));
	}
}

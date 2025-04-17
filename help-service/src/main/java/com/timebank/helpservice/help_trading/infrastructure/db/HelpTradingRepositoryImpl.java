package com.timebank.helpservice.help_trading.infrastructure.db;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.timebank.helpservice.help_trading.domain.model.HelpTrading;
import com.timebank.helpservice.help_trading.domain.repository.HelpTradingRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HelpTradingRepositoryImpl implements HelpTradingRepository {

	private final JpaHelpTradingRepository jpaHelpTradingRepository;

	@Override
	public HelpTrading save(HelpTrading helpTrading) {
		return jpaHelpTradingRepository.save(helpTrading);
	}

	@Override
	public Optional<HelpTrading> findById(Long helpTradingId) {
		return jpaHelpTradingRepository.findById(helpTradingId);
	}

	@Override
	public Page<HelpTrading> findByHelpRequestId(Long helpRequestId, Pageable pageable) {
		return jpaHelpTradingRepository.findByHelpRequestId(helpRequestId, pageable);
	}
}

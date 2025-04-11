package com.timebank.helpservice.help_trading.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

public interface HelpTradingRepository {

	HelpTrading save(HelpTrading helpTrading);

	Optional<HelpTrading> findById(Long helpTradingId);

	Page<HelpTrading> findByHelpRequestId(Long helpRequestId, Pageable pageable);
}

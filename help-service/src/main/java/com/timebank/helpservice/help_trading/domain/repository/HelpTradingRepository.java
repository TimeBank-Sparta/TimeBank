package com.timebank.helpservice.help_trading.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

public interface HelpTradingRepository {

	Page<HelpTrading> findByHelpRequestId(Long helpRequestId, Pageable pageable);
}

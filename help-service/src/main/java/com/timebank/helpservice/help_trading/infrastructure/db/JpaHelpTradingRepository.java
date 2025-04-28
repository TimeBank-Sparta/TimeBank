package com.timebank.helpservice.help_trading.infrastructure.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.help_trading.domain.model.HelpTrading;

public interface JpaHelpTradingRepository extends JpaRepository<HelpTrading, Long> {
	Page<HelpTrading> findByHelpRequestId(Long helpRequestId, Pageable pageable);
}

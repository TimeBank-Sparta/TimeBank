package com.timebank.helpservice.help_trading.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.help_trading.domain.model.HelpTrading;
import com.timebank.helpservice.help_trading.domain.repository.HelpTradingRepository;

public interface JpaHelpRepository extends HelpTradingRepository, JpaRepository<HelpTrading, Long> {
}

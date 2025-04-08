package com.timebank.helpservice.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.domain.model.HelpRequest;
import com.timebank.helpservice.domain.repository.HelpRepository;

public interface JpaHelpRepository extends HelpRepository, JpaRepository<HelpRequest, Long> {
}

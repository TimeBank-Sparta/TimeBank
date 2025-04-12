package com.timebank.helpservice.help_request.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.HelpRequestRepository;

public interface JpaHelpRequestRepository
	extends HelpRequestRepository, JpaRepository<HelpRequest, Long>, JpaHelpRequestRepositoryCustom {
}

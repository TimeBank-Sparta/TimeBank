package com.timebank.helpservice.help_request.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timebank.helpservice.help_request.domain.model.HelpRequest;

public interface JpaHelpRequestRepository extends JpaRepository<HelpRequest, Long>, JpaHelpRequestRepositoryCustom {
}

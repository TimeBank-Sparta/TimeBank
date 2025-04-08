package com.timebank.helpservice.domain.repository;

import java.util.Optional;

import com.timebank.helpservice.domain.model.HelpRequest;

public interface HelpRepository {

	HelpRequest save(HelpRequest helpRequest);

	Optional<HelpRequest> findById(Long helpRequestId);
}

package com.timebank.helpservice.help_request.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;

public interface HelpRequestRepository {

	HelpRequest save(HelpRequest helpRequest);

	Optional<HelpRequest> findById(Long helpRequestId);

	boolean existsById(Long helpRequestId);

	Page<HelpRequest> search(HelpRequestQuery request, Pageable pageable);
}

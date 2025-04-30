package com.timebank.helpservice.help_request.infrastructure.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;
import com.timebank.helpservice.help_request.domain.repository.search.SearchNearByQuery;

public interface JpaHelpRequestRepositoryCustom {
	Page<HelpRequest> search(HelpRequestQuery query,
		Pageable pageable);

	Page<HelpRequest> findHelpRequestNearby(SearchNearByQuery query,
		double radiusKm, Pageable pageable);
}

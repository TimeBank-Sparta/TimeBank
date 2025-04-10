package com.timebank.helpservice.help_request.infrastructure.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;

public interface JpaHelpRequestRepositoryCustom {

	Page<HelpRequest> search(HelpRequestQuery requestDto, Pageable pageable);
}

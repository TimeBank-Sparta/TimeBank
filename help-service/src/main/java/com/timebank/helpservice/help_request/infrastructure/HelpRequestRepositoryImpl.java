package com.timebank.helpservice.help_request.infrastructure;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.HelpRequestRepository;
import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;
import com.timebank.helpservice.help_request.domain.repository.search.SearchNearByQuery;
import com.timebank.helpservice.help_request.infrastructure.jpa.JpaHelpRequestRepository;
import com.timebank.helpservice.helper.infrastructure.redis.RedisHelperRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HelpRequestRepositoryImpl implements HelpRequestRepository {
	private final JpaHelpRequestRepository jpaHelpRequestRepository;
	private final RedisHelperRepository redisHelperRepository;

	@Override
	public HelpRequest save(HelpRequest helpRequest) {
		return jpaHelpRequestRepository.save(helpRequest);
	}

	@Override
	public Optional<HelpRequest> findById(Long helpRequestId) {
		return jpaHelpRequestRepository.findById(helpRequestId);
	}

	@Override
	public boolean existsById(Long helpRequestId) {
		return jpaHelpRequestRepository.existsById(helpRequestId);
	}

	@Override
	public Page<HelpRequest> search(HelpRequestQuery query, Pageable pageable) {
		return jpaHelpRequestRepository.search(query, pageable);
	}

	@Override
	public Page<HelpRequest> findHelpRequestNearby(SearchNearByQuery query, double radiusKm, Pageable pageable) {
		return jpaHelpRequestRepository.findHelpRequestNearby(query, radiusKm, pageable);
	}

}

package com.timebank.helpservice.helper.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;
import com.timebank.helpservice.helper.domain.repository.HelperRepository;
import com.timebank.helpservice.helper.infrastructure.jpa.JpaHelperRepository;
import com.timebank.helpservice.helper.infrastructure.redis.RedisHelperRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HelperRepositoryImpl implements HelperRepository {
	private final JpaHelperRepository jpaHelperRepository;
	private final RedisHelperRepository redisHelperRepository;

	@Override
	public Helper save(Helper helper) {
		return jpaHelperRepository.save(helper);
	}

	@Override
	public Optional<Helper> findById(Long id) {
		return jpaHelperRepository.findById(id);
	}

	@Override
	public boolean existsByHelpRequestIdAndUserId(Long helpRequestId, Long id) {
		return jpaHelperRepository.existsByHelpRequestIdAndUserId(helpRequestId, id);
	}

	@Override
	public long countByHelpRequestIdAndApplicantStatus(Long id, ApplicantStatus applicantStatus) {
		return jpaHelperRepository.countByHelpRequestIdAndApplicantStatus(id, applicantStatus);
	}

	@Override
	public List<Helper> findByHelpRequestId(Long helpRequestId) {
		return jpaHelperRepository.findByHelpRequestId(helpRequestId);
	}

	@Override
	public void deleteHelperStatusSupported(Long helpRequestId) {
		jpaHelperRepository.deleteHelperStatusSupported(helpRequestId);
	}
}

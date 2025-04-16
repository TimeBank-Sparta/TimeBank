package com.timebank.helpservice.helper.infrastructure.db;

import static com.timebank.helpservice.helper.domain.model.QHelper.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.timebank.helpservice.helper.domain.ApplicantStatus;
import com.timebank.helpservice.helper.domain.model.Helper;
import com.timebank.helpservice.helper.domain.repository.HelperRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaHelperRepositoryCustomImpl implements HelperRepository {
	private final JpaHelperRepository jpaHelperRepository;
	private final JPAQueryFactory queryFactory;

	@Override
	public Helper save(Helper helper) {
		return jpaHelperRepository.save(helper);
	}

	@Override
	public Optional<Helper> findById(Long id) {
		return jpaHelperRepository.findById(id);
	}

	@Override
	public boolean existsByUserId(Long id) {
		return jpaHelperRepository.existsById(id);
	}

	@Override
	public long countByHelpRequestIdAndApplicantStatus(Long id, ApplicantStatus applicantStatus) {
		return jpaHelperRepository.countByHelpRequestIdAndApplicantStatus(id, applicantStatus);
	}

	@Override
	public List<Helper> findByHelpRequestId(Long helpRequestId) {
		return List.of();
	}

	@Override
	public void deleteHelperStatusSupported(Long helpRequestId) {
		queryFactory
			.update(helper)
			.set(helper.deletedAt, LocalDateTime.now())
			.where(
				helper.applicantStatus.eq(ApplicantStatus.SUPPORTED),
				helpRequestIdEq(helpRequestId)
			)
			.execute();
	}

	private BooleanExpression helpRequestIdEq(Long helpRequestId) {
		return Objects.nonNull(helpRequestId) ? helper.helpRequestId.eq(helpRequestId) : null;
	}
}

package com.timebank.helpservice.helper.infrastructure.db;

import static com.timebank.helpservice.helper.domain.model.QHelper.*;

import java.time.LocalDateTime;
import java.util.Objects;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.timebank.helpservice.helper.domain.ApplicantStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaHelperRepositoryCustomImpl implements JpaHelperRepositoryCustom {

	private final JPAQueryFactory queryFactory;

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

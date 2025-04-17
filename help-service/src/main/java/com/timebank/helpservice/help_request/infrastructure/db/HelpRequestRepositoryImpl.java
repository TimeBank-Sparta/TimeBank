package com.timebank.helpservice.help_request.infrastructure.db;

import static com.timebank.helpservice.help_request.domain.model.QHelpRequest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.timebank.helpservice.help_request.domain.HelpRequestSortType;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.HelpRequestRepository;
import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class HelpRequestRepositoryImpl implements HelpRequestRepository {
	private final JpaHelpRequestRepository jpaHelpRequestRepository;
	private final JPAQueryFactory queryFactory;

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
	public Page<HelpRequest> search(HelpRequestQuery requestQuery,
		Pageable pageable) {
		int pageSize = validatePageSize(pageable.getPageSize());

		Long totalCount = queryFactory
			.select(helpRequest.count())
			.from(helpRequest)
			.where(
				userIdEq(requestQuery.requesterId()),
				containsTitle(requestQuery.title())
			)
			.fetchOne();

		List<HelpRequest> fetch = queryFactory
			.selectFrom(helpRequest)
			.where(
				userIdEq(requestQuery.requesterId()),
				containsTitle(requestQuery.title()))
			.orderBy(createOrderSpecifier(pageable).toArray(new OrderSpecifier[0]))
			.offset(pageable.getOffset())
			.limit(pageSize)
			.distinct()
			.fetch();

		return new PageImpl<>(fetch, pageable, totalCount == null ? 0 : totalCount);
	}

	private List<OrderSpecifier<?>> createOrderSpecifier(Pageable pageable) {
		List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

		Map<String, HelpRequestSortType> sortTypeMap = Map.of(
			HelpRequestSortType.CREATED_AT.getName(), HelpRequestSortType.CREATED_AT,
			HelpRequestSortType.UPDATED_AT.getName(), HelpRequestSortType.UPDATED_AT,
			HelpRequestSortType.TITLE.getName(), HelpRequestSortType.TITLE
		);

		if (pageable.getSort().isSorted()) {
			pageable.getSort().forEach(order -> {
				if (sortTypeMap.containsKey(order.getProperty())) {
					orderSpecifierList.add(sortTypeMap.get(order.getProperty())
						.getOrderSpecifier(order.isAscending()));
				}
			});
		}
		return orderSpecifierList;
	}

	private int validatePageSize(int pageSize) {
		return Set.of(10, 30, 50).contains(pageSize) ? pageSize : 10;
	}

	private BooleanExpression userIdEq(Long userId) {
		return Objects.nonNull(userId) ? helpRequest.id.eq(userId) : null;
	}

	private BooleanExpression containsTitle(String title) {
		return Objects.nonNull(title) ? helpRequest.title.containsIgnoreCase(title) : null;
	}

}

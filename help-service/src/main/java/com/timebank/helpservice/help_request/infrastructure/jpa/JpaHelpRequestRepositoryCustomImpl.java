package com.timebank.helpservice.help_request.infrastructure.jpa;

import static com.timebank.helpservice.help_request.domain.model.QHelpRequest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.timebank.helpservice.help_request.domain.HelpRequestSortType;
import com.timebank.helpservice.help_request.domain.model.HelpRequest;
import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;
import com.timebank.helpservice.help_request.domain.repository.search.SearchNearByQuery;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaHelpRequestRepositoryCustomImpl implements JpaHelpRequestRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<HelpRequest> search(HelpRequestQuery query,
		Pageable pageable
	) {
		int pageSize = validatePageSize(pageable.getPageSize());

		Long totalCount = queryFactory
			.select(helpRequest.count())
			.from(helpRequest)
			.where(
				userIdEq(query.requesterId()),
				containsTitle(query.title())
			)
			.fetchOne();

		List<HelpRequest> fetch = queryFactory
			.selectFrom(helpRequest)
			.where(
				userIdEq(query.requesterId()),
				containsTitle(query.title()))
			.orderBy(createOrderSpecifier(pageable).toArray(new OrderSpecifier[0]))
			.offset(pageable.getOffset())
			.limit(pageSize)
			.fetch();

		return new PageImpl<>(fetch, pageable, totalCount == null ? 0 : totalCount);
	}

	public Page<HelpRequest> findHelpRequestNearby(SearchNearByQuery query,
		double radiusKm, Pageable pageable
	) {
		NumberExpression<Double> distanceExpr = distanceExpr(
			query.userLatitude(), query.userLongitude());

		Long totalCount = queryFactory
			.select(helpRequest.count())
			.from(helpRequest)
			.where(distanceExpr.loe(radiusKm))
			.fetchOne();
		
		List<HelpRequest> content = queryFactory
			.selectFrom(helpRequest)
			.where(distanceExpr.loe(radiusKm))
			.orderBy(distanceExpr.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(content, pageable, totalCount == null ? 0 : totalCount);
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

	private NumberExpression<Double> distanceExpr(double lat, double lng) {
		return Expressions.numberTemplate(Double.class, """
			6371 * acos(
				cos(radians({0})) * cos(radians({1})) *
				cos(radians({2}) - radians({3})) +
				sin(radians({0})) * sin(radians({1}))
			)
			""", helpRequest.location.latitude, lat, lng, helpRequest.location.longitude);
	}
}

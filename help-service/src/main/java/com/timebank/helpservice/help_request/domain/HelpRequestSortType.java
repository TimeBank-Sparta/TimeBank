package com.timebank.helpservice.help_request.domain;

import static com.timebank.helpservice.help_request.domain.model.QHelpRequest.*;

import java.util.function.Function;

import com.querydsl.core.types.OrderSpecifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HelpRequestSortType {

	CREATED_AT("createdAt",
		direction -> direction ? helpRequest.createdAt.asc() : helpRequest.createdAt.desc()),
	UPDATED_AT("updatedAt",
		direction -> direction ? helpRequest.updatedAt.asc() : helpRequest.updatedAt.desc()),
	TITLE("title",
		direction -> direction ? helpRequest.title.asc() : helpRequest.title.desc());

	@Getter
	private final String name;
	private final Function<Boolean, OrderSpecifier<?>> expression;

	public OrderSpecifier<?> getOrderSpecifier(boolean ascending) {
		return expression.apply(ascending);
	}
}
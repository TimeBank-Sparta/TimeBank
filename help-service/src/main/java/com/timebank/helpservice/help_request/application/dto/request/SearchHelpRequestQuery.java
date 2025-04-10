package com.timebank.helpservice.help_request.application.dto.request;

import com.timebank.helpservice.help_request.domain.repository.search.HelpRequestQuery;

import lombok.Builder;

@Builder
public record SearchHelpRequestQuery(
	Long requesterId,
	String title
) {
	public HelpRequestQuery toHelpRequestQuery() {
		return HelpRequestQuery.builder()
			.title(title)
			.requesterId(requesterId)
			.build();
	}
}

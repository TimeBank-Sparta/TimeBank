package com.timebank.helpservice.help_request.presentation.dto.request;

import com.timebank.helpservice.help_request.application.dto.request.SearchHelpRequestQuery;

public record SearchHelpRequest(
	Long requesterId,
	String title
) {
	public SearchHelpRequestQuery toQuery() {
		return SearchHelpRequestQuery.builder()
			.requesterId(requesterId)
			.title(title)
			.build();
	}
}

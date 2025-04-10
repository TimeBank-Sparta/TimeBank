package com.timebank.helpservice.help_request.domain.repository.search;

import lombok.Builder;

@Builder
public record HelpRequestQuery(
	Long requesterId,
	String title
) {
}

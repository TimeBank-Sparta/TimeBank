package com.timebank.helpservice.help_request.domain.repository.search;

import lombok.Builder;

@Builder
public record SearchNearByQuery(
	double userLatitude,
	double userLongitude
) {
}

package com.timebank.helpservice.help_request.application.dto.request;

import com.timebank.helpservice.help_request.domain.repository.search.SearchNearByQuery;

import lombok.Builder;

@Builder
public record NearByUserLocationQuery(
	double userLatitude,
	double userLongitude
) {

	public SearchNearByQuery toQuery() {
		return SearchNearByQuery.builder()
			.userLatitude(userLatitude)
			.userLongitude(userLongitude)
			.build();
	}
}

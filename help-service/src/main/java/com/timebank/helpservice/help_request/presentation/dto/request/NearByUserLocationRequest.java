package com.timebank.helpservice.help_request.presentation.dto.request;

import com.timebank.helpservice.help_request.application.dto.request.NearByUserLocationQuery;

import lombok.Builder;

@Builder
public record NearByUserLocationRequest(
	double userLatitude,
	double userLongitude
) {
	public NearByUserLocationQuery toQuery() {
		return NearByUserLocationQuery.builder()
			.userLatitude(userLatitude)
			.userLongitude(userLongitude)
			.build();
	}
}

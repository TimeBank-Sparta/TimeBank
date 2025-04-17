package com.timebank.userservice.application.dto.response.profile;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoGeocodeResponse {
	private List<Document> documents;

	@Getter
	public static class Document {
		private Address address;
		private RoadAddress road_address;
	}

	@Getter
	public static class Address {
		@JsonProperty("address_name")
		private String addressName;
	}

	@Getter
	public static class RoadAddress {
		@JsonProperty("address_name")
		private String addressName;
	}
}
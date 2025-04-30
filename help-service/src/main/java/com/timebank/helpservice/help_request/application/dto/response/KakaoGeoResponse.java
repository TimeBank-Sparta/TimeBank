package com.timebank.helpservice.help_request.application.dto.response;

import java.util.List;

public record KakaoGeoResponse(List<Document> documents) {

	public record Document(String addressName, String x, String y) {
	}
}
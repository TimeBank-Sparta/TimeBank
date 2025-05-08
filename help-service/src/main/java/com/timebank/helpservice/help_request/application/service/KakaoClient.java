package com.timebank.helpservice.help_request.application.service;

import org.springframework.web.bind.annotation.RequestParam;

import com.timebank.helpservice.help_request.application.dto.response.KakaoGeoResponse;

public interface KakaoClient {
	KakaoGeoResponse searchAddress(@RequestParam String query);
}

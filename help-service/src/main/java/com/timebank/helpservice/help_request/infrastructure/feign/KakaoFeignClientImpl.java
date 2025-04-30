package com.timebank.helpservice.help_request.infrastructure.feign;

import org.springframework.stereotype.Component;

import com.timebank.helpservice.help_request.application.dto.response.KakaoGeoResponse;
import com.timebank.helpservice.help_request.application.service.KakaoClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KakaoFeignClientImpl implements KakaoClient {
	private final KakaoFeignClient kakaoFeignClient;

	@Override
	public KakaoGeoResponse searchAddress(String query) {
		return kakaoFeignClient.searchAddress(query);
	}
}

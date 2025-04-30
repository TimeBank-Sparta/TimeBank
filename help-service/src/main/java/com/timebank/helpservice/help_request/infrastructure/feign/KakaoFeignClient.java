package com.timebank.helpservice.help_request.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.timebank.helpservice.help_request.application.dto.response.KakaoGeoResponse;
import com.timebank.helpservice.help_request.infrastructure.feign.config.KakaoFeignConfig;

@FeignClient(
	name = "KakaoFeignClient",
	url = "https://dapi.kakao.com",
	configuration = KakaoFeignConfig.class
)
public interface KakaoFeignClient {

	@GetMapping("/v2/local/search/address.json")
	KakaoGeoResponse searchAddress(@RequestParam String query);
}

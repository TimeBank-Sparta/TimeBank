package com.timebank.helpservice.help_request.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.timebank.helpservice.help_request.application.dto.request.HoldPointRequestDto;

@FeignClient("point-service")
public interface PointFeignClient {

	@PostMapping("/api/v1/points/hold")
	void holdPoint(@RequestBody HoldPointRequestDto request);

}

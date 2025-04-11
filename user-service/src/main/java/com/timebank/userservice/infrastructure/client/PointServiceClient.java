package com.timebank.userservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.timebank.userservice.infrastructure.client.dto.PointAccountResponseDto;

@FeignClient(name = "point-service")
public interface PointServiceClient {
	@GetMapping("/api/v1/point")
	PointAccountResponseDto getAccount(@PathVariable("accountId") Long accountId);
}

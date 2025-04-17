package com.timebank.userservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.timebank.userservice.infrastructure.client.dto.PointAccountResponseDto;

@FeignClient(name = "point-service")
public interface PointServiceClient {
	@PostMapping("/api/v1/points/{userId}")
	void creatAccount(@PathVariable Long userId);

	@GetMapping("/api/v1/points/{userId}")
	PointAccountResponseDto getAccount(@PathVariable("userId") Long userId);
}

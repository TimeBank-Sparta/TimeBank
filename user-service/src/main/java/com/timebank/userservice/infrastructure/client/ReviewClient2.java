package com.timebank.userservice.infrastructure.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "review-service")
public interface ReviewClient2 {
	@GetMapping("/internal/reviews/user/{userId}")
	List<Byte> getRatingsByUserId(@PathVariable Long userId);
}


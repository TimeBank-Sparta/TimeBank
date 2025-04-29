package com.timebank.helpservice.helper.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.timebank.helpservice.helper.application.dto.request.HelpRequestFeignDto;

@FeignClient("help-service")
public interface HelpRequestFeignClient {

	@GetMapping("/api/v1/help-requests/{helpRequestId}/info")
	HelpRequestFeignDto getHelpRequestById(@PathVariable Long helpRequestId);

	@GetMapping("/api/v1/help-requests/{helpRequestId}/exists")
	boolean existHelpRequestById(@PathVariable Long helpRequestId);
}

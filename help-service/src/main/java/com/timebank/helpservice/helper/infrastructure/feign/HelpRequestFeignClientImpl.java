package com.timebank.helpservice.helper.infrastructure.feign;

import org.springframework.stereotype.Component;

import com.timebank.helpservice.helper.application.client.HelpRequestClient;
import com.timebank.helpservice.helper.application.dto.request.HelpRequestFeignDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelpRequestFeignClientImpl implements HelpRequestClient {

	private final HelpRequestFeignClient helpRequestFeignClient;

	@Override
	public HelpRequestFeignDto getHelpRequestById(Long helpRequestId) {
		return helpRequestFeignClient.getHelpRequestById(helpRequestId);
	}

	@Override
	public boolean existHelpRequestById(Long helpRequestId) {
		return helpRequestFeignClient.existHelpRequestById(helpRequestId);
	}
}

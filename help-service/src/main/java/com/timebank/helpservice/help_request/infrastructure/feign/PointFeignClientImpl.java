package com.timebank.helpservice.help_request.infrastructure.feign;

import org.springframework.stereotype.Component;

import com.timebank.helpservice.help_request.application.dto.request.HoldPointRequestDto;
import com.timebank.helpservice.help_request.application.service.PointClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PointFeignClientImpl implements PointClient {

	private final PointFeignClient pointFeignClient;

	@Override
	public void holdPoint(HoldPointRequestDto requestDto) {
		pointFeignClient.holdPoint(requestDto);
	}
}

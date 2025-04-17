package com.timebank.helpservice.help_request.application.service;

import com.timebank.helpservice.help_request.application.dto.request.HoldPointRequestDto;

public interface PointClient {
	void holdPoint(HoldPointRequestDto requestDto);
}

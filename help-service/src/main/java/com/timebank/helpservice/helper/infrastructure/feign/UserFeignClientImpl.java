package com.timebank.helpservice.helper.infrastructure.feign;

import java.util.List;

import org.springframework.stereotype.Component;

import com.timebank.helpservice.helper.application.client.UserClient;
import com.timebank.helpservice.helper.application.dto.request.GetUserInfoFeignRequest;
import com.timebank.helpservice.helper.application.dto.response.GetUserInfoFeignResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserFeignClientImpl implements UserClient {

	private final UserFeignClient userFeignClient;

	@Override
	public List<GetUserInfoFeignResponse> getUserInfoByHelper(List<GetUserInfoFeignRequest> request) {
		return userFeignClient.getUserInfoByHelper(request);
	}
}

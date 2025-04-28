package com.timebank.helpservice.helper.application.client;

import java.util.List;

import com.timebank.helpservice.helper.application.dto.request.GetUserInfoFeignRequest;
import com.timebank.helpservice.helper.application.dto.response.GetUserInfoFeignResponse;

public interface UserClient {
	List<GetUserInfoFeignResponse> getUserInfoByHelper(List<GetUserInfoFeignRequest> request);
}

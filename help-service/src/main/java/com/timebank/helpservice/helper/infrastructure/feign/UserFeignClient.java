package com.timebank.helpservice.helper.infrastructure.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.timebank.helpservice.helper.application.dto.request.GetUserInfoFeignRequest;
import com.timebank.helpservice.helper.application.dto.response.GetUserInfoFeignResponse;

@FeignClient("user-service")
public interface UserFeignClient {

	@PostMapping("/api/v1/user")
	List<GetUserInfoFeignResponse> getUserInfoByHelper(@RequestBody List<GetUserInfoFeignRequest> request);

}

package com.timebank.helpservice.helper.application.client;

import com.timebank.helpservice.helper.application.dto.request.HelpRequestFeignDto;

public interface HelpRequestClient {
	HelpRequestFeignDto getHelpRequestById(Long helpRequestId);

	boolean existHelpRequestById(Long helpRequestId);
}

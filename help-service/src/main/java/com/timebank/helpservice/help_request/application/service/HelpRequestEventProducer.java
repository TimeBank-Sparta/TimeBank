package com.timebank.helpservice.help_request.application.service;

import com.timebank.helpservice.help_request.application.dto.request.HelpRequestToHelperKafkaDto;

public interface HelpRequestEventProducer {
	void sendToHelper(HelpRequestToHelperKafkaDto helpRequestToHelperKafkaDto);
}

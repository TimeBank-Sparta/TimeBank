package com.timebank.helpservice.helper.application.service;

import com.timebank.helpservice.helper.application.dto.request.HelperToTradingKafkaDto;

public interface HelperEventProducer {
	void sendToHelpTrading(HelperToTradingKafkaDto helperToTradingKafkaDto);
}

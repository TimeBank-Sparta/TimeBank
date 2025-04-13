package com.timebank.helpservice.helper.infrastructure.messaing;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.timebank.helpservice.helper.application.dto.request.HelperToTradingKafkaDto;
import com.timebank.helpservice.helper.application.service.HelperEventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelperKafkaProducer implements HelperEventProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void sendToHelpTrading(HelperToTradingKafkaDto helperToTradingKafkaDto) {
		kafkaTemplate.send("helpers.create-trading", "help-tradings", helperToTradingKafkaDto);
	}
}

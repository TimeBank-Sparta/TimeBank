package com.timebank.helpservice.help_trading.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.timebank.helpservice.help_trading.application.dto.request.PointTransferRequestMessage;
import com.timebank.helpservice.help_trading.application.service.HelpTradingEventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelpTradingKafkaProducer implements HelpTradingEventProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void sendToPoints(PointTransferRequestMessage pointTransferRequestMessage) {
		kafkaTemplate.send("help-trading.complete", "point-service-group", pointTransferRequestMessage);
	}
}

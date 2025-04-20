package com.timebank.helpservice.help_trading.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.timebank.common.infrastructure.dto.PointTransferRequestMessage;
import com.timebank.helpservice.help_trading.application.service.HelpTradingEventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelpTradingKafkaProducer implements HelpTradingEventProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void sendToPointsHoldConfirm(PointTransferRequestMessage pointTransferRequestMessage) {
		kafkaTemplate.send("help-trading.complete", pointTransferRequestMessage);
	}

	@Override
	public void sendToPointsCancel(PointTransferRequestMessage pointTransferRequestMessage) {
		kafkaTemplate.send("help-trading.cancel", pointTransferRequestMessage);
	}
}

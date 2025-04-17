package com.timebank.helpservice.help_request.infrastructure.messeging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.timebank.helpservice.help_request.application.dto.request.HelpRequestToHelperKafkaDto;
import com.timebank.helpservice.help_request.application.service.HelpRequestEventProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HelpRequestKafkaProducer implements HelpRequestEventProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Override
	public void sendToHelper(HelpRequestToHelperKafkaDto helpRequestToHelperKafkaDto) {
		kafkaTemplate.send("help-request.delete", helpRequestToHelperKafkaDto);
	}

}

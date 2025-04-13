package com.timebank.helpservice.helper.infrastructure.messaing;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.timebank.helpservice.help_trading.infrastructure.messaging.KafkaMessageParser;
import com.timebank.helpservice.helper.application.dto.response.FromHelpRequestKafkaDto;
import com.timebank.helpservice.helper.application.service.HelperService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelperEventKafkaListener {

	private final HelperService helperService;

	@KafkaListener(topics = "help-request.delete", groupId = "helpers")
	public void consumeFromGroupA(String message) {
		FromHelpRequestKafkaDto parsedDto = KafkaMessageParser.parse(message, FromHelpRequestKafkaDto.class);
		helperService.deleteHelpersByStatusSupported(parsedDto);
	}

}

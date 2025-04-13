package com.timebank.helpservice.help_trading.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.timebank.helpservice.help_trading.application.dto.response.FromHelperKafkaDto;
import com.timebank.helpservice.help_trading.application.service.HelpTradingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpTradingEventKafkaListener {

	private final HelpTradingService helpTradingService;

	@KafkaListener(topics = "helpers.create-trading", groupId = "help-tradings")
	public void consumeFromHelper(String message) {
		FromHelperKafkaDto parsedDto = KafkaMessageParser.parse(message, FromHelperKafkaDto.class);
		helpTradingService.createHelpTradingFromKafka(parsedDto);
		log.info("Created help trading from kafka");
	}

}

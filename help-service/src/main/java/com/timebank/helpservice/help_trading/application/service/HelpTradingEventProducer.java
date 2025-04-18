package com.timebank.helpservice.help_trading.application.service;

import com.timebank.common.infrastructure.dto.PointTransferRequestMessage;

public interface HelpTradingEventProducer {
	void sendToPoints(PointTransferRequestMessage pointTransferRequestMessage);
}

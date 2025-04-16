package com.timebank.helpservice.help_trading.application.service;

import com.timebank.helpservice.help_trading.application.dto.request.PointTransferRequestMessage;

public interface HelpTradingEventProducer {
	void sendToPoints(PointTransferRequestMessage pointTransferRequestMessage);
}

package com.timebank.pointservice.presentation.controller.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timebank.pointservice.infrastructure.kafka.dto.PointTransferRequestMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class KafkaProduceController {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/p1")
	public ResponseEntity<String> sendP1(@RequestBody PointTransferRequestMessage dto) throws Exception {
		String message = objectMapper.writeValueAsString(dto);
		kafkaTemplate.send(new ProducerRecord<>("point.transfer.p1", message));
		return ResponseEntity.ok("sent to p1");
	}

	@PostMapping("/p4")
	public ResponseEntity<String> sendP4(@RequestBody PointTransferRequestMessage dto) throws Exception{
		String message = objectMapper.writeValueAsString(dto);
		kafkaTemplate.send(new ProducerRecord<>("point.transfer.p4", message));
		kafkaTemplate.send("point.transfer.p4", message);
		return ResponseEntity.ok("sent to p4");
	}
}

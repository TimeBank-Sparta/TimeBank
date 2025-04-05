package com.timebank.pointservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;

@SpringBootTest
public class PointServiceTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	private Long senderAccountId;
	private Long receiverAccountId;

	@BeforeEach
	void setup() {
		PointAccount sender = PointAccount.builder()
			.userId(100L)
			.totalPoints(1000)
			.build();

		PointAccount receiver = PointAccount.builder()
			.userId(200L)
			.totalPoints(0)
			.build();

		sender = pointAccountRepository.save(sender);
		receiver = pointAccountRepository.save(receiver);

		senderAccountId = sender.getAccountId();    // 클래스 필드에 저장
		receiverAccountId = receiver.getAccountId();
	}

	@Test
	void 동시성_테스트_송금_충돌() throws InterruptedException {
		int threadCount = 10;
		int amountToSend = 50;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					PointTransferCommand command = new PointTransferCommand(
						senderAccountId,
						receiverAccountId,
						amountToSend,
						"동시 테스트"
					);
					pointService.transferPoints(command);
				} catch (Exception e) {
					System.out.println("에러 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		PointAccount sender = pointAccountRepository.findById(senderAccountId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findById(receiverAccountId).orElseThrow();

		System.out.println("Sender 잔액: " + sender.getTotalPoints());
		System.out.println("Receiver 잔액: " + receiver.getTotalPoints());

		assertTrue(sender.getTotalPoints() >= 0, "포인트 음수 발생!");
	}
}

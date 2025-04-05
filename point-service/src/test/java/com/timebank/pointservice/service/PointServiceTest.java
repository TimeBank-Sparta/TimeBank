package com.timebank.pointservice.service;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PointServiceConcurrencyTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	private Long senderId;
	private Long receiverId;

	@BeforeEach
	void setUp() {
		PointAccount sender = PointAccount.builder()
			.userId(1L)
			.totalPoints(1000)
			.build();

		PointAccount receiver = PointAccount.builder()
			.userId(2L)
			.totalPoints(0)
			.build();

		sender = pointAccountRepository.save(sender);
		receiver = pointAccountRepository.save(receiver);

		senderId = sender.getAccountId();
		receiverId = receiver.getAccountId();
	}

	@Test
	void 동시에_여러_송금_요청이_들어와도_정합성을_유지한다() throws InterruptedException {
		int threadCount = 10;
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		int amountPerTransfer = 50;

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					pointService.transferPoints(new PointTransferCommand(
						senderId,
						receiverId,
						amountPerTransfer,
						"동시송금"
					));
				} catch (NoSuchElementException | IllegalArgumentException e) {
					System.out.println("예외 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		PointAccount sender = pointAccountRepository.findById(senderId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findById(receiverId).orElseThrow();

		System.out.println("송신자 최종 잔액: " + sender.getTotalPoints());
		System.out.println("수신자 최종 잔액: " + receiver.getTotalPoints());

		// 총 10번 x 50포인트 = 500 포인트가 이동되었는지 검증
		assertEquals(1000 - (amountPerTransfer * threadCount), sender.getTotalPoints());
		assertEquals(amountPerTransfer * threadCount, receiver.getTotalPoints());
	}
}

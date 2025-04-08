package com.timebank.pointservice.service;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.application.service.PointService;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointTransferConcurrencyTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	@Autowired
	private PointTransactionRepository pointTransactionRepository;

	private final Long senderId = 1001L;
	private final Long receiverId = 1002L;

	@BeforeEach
	void setUp() {
		pointTransactionRepository.deleteAll();
		pointAccountRepository.deleteAll();

		// 송신자: 1000포인트 보유
		pointAccountRepository.save(PointAccount.builder()
			.userId(senderId)
			.availablePoints(1000)
			.holdingPoints(0)
			.build());

		// 수신자: 0포인트
		pointAccountRepository.save(PointAccount.builder()
			.userId(receiverId)
			.availablePoints(0)
			.holdingPoints(0)
			.build());
	}

	@Test
	void 동시에_즉시_송금_요청이_들어와도_정합성이_보장되어야_한다() throws InterruptedException {
		int threadCount = 20;
		int transferAmount = 50;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					pointService.transferPoints(PointTransferCommand.builder()
						.senderUserId(senderId)
						.receiverUserId(receiverId)
						.amount(transferAmount)
						.reason("즉시 송금 테스트")
						.build());
				} catch (Exception e) {
					System.out.println("❌ 예외 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		PointAccount sender = pointAccountRepository.findByUserId(senderId).orElseThrow();
		PointAccount receiver = pointAccountRepository.findByUserId(receiverId).orElseThrow();

		System.out.println("✅ 최종 송신자 available: " + sender.getAvailablePoints());
		System.out.println("✅ 최종 수신자 available: " + receiver.getAvailablePoints());

		assertThat(sender.getAvailablePoints() + receiver.getAvailablePoints()).isEqualTo(1000);
		assertThat(sender.getAvailablePoints()).isGreaterThanOrEqualTo(0);
		assertThat(receiver.getAvailablePoints()).isLessThanOrEqualTo(1000);
	}
}

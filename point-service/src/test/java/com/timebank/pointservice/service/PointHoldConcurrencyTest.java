package com.timebank.pointservice.service;

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
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointHoldConcurrencyTest {

	@Autowired
	private PointService pointService;

	@Autowired
	private PointAccountRepository pointAccountRepository;

	@Autowired
	private PointTransactionRepository pointTransactionRepository;

	private final Long testUserId = 1000L;

	@BeforeEach
	void setup() {
		pointTransactionRepository.deleteAll();
		pointAccountRepository.deleteAll();

		PointAccount account = PointAccount.builder()
			.userId(testUserId)
			.availablePoints(1000)
			.holdingPoints(0)
			.build();

		pointAccountRepository.save(account);
	}

	@Test
	void 동시에_보류요청_시_정합성_보장되어야_한다() throws InterruptedException {
		int threadCount = 50;
		int holdAmount = 50;

		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		AtomicInteger successCount = new AtomicInteger();

		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					pointService.holdPointsForPost(testUserId, holdAmount);
					successCount.incrementAndGet();
				} catch (Exception e) {
					System.out.println("❌ 실패한 요청: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executor.shutdown();

		PointAccount finalAccount = pointAccountRepository.findByUserId(testUserId)
			.orElseThrow(() -> new IllegalStateException("계좌가 존재하지 않습니다."));

		int available = finalAccount.getAvailablePoints();
		int holding = finalAccount.getHoldingPoints();
		int total = available + holding;

		System.out.println("✅ 최종 사용 가능 포인트: " + available);
		System.out.println("✅ 최종 보류 중 포인트: " + holding);
		System.out.println("✅ 최종 총합: " + total);
		System.out.println("✅ 성공 요청 수: " + successCount.get());

		// ✅ 총합은 항상 1000이어야 함
		assertThat(total).isEqualTo(1000);

		// ✅ 50포인트씩 보류했으므로 최대 20건 성공 가능
		assertThat(successCount.get()).isLessThanOrEqualTo(20);
		assertThat(holding).isEqualTo(successCount.get() * holdAmount);
	}
}

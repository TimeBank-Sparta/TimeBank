package com.timebank.pointservice.application.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.entity.PointTransaction;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointAccountRepository pointAccountRepository;
	private final PointTransactionRepository pointTransactionRepository;
	private final MeterRegistry meterRegistry;

	// 계정 생성
	public PointAccount createAccount(Long userId) {
		PointAccount newAccount = PointAccount.builder()
			.userId(userId)
			.availablePoints(600)
			.holdingPoints(0)
			.build();

		return pointAccountRepository.save(newAccount);
	}

	// 계정 조회
	public PointAccount getAccount(Long userId) {
		return pointAccountRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
	}

	// ✅ 글 작성 시 포인트 보류
	@Transactional
	public void holdPointsForPost(Long userId, int amount, String reason) {
		PointAccount account = pointAccountRepository.findByUserIdForUpdate(userId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다."));

		account.holdPoints(amount); // 보류 로직 호출

		PointTransaction holdTx = PointTransaction.builder()
			.account(account)
			.amount(-amount)
			.transactionReason("Hold: " + reason)
			.build();

		pointTransactionRepository.save(holdTx);
		account.getPointTransactions().add(holdTx);
	}

	// ✅ 거래 확정 시 보류 포인트 → 수신자 전송
	@Transactional
	public void confirmTransfer(PointTransferCommand command) {
		Long senderUserId = command.getSenderUserId();
		Long receiverUserId = command.getReceiverUserId();
		int amount = command.getAmount();
		String reason = command.getReason();

		if (amount <= 0) {
			throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
		}

		Long firstUserId = Math.min(senderUserId, receiverUserId);
		Long secondUserId = Math.max(senderUserId, receiverUserId);

		PointAccount firstAccount = pointAccountRepository.findByUserIdForUpdate(firstUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + firstUserId));

		PointAccount secondAccount = pointAccountRepository.findByUserIdForUpdate(secondUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + secondUserId));

		PointAccount sender = senderUserId.equals(firstUserId) ? firstAccount : secondAccount;
		PointAccount receiver = receiverUserId.equals(firstUserId) ? firstAccount : secondAccount;

		sender.confirmHolding(amount); // 보류 → 차감
		receiver.increaseAvailablePoints(amount); // 수신자 지급

		PointTransaction sendTx = PointTransaction.builder()
			.account(sender)
			.amount(-amount)
			.transactionReason("Confirm: " + reason)
			.build();

		PointTransaction receiveTx = PointTransaction.builder()
			.account(receiver)
			.amount(amount)
			.transactionReason("Receive: " + reason)
			.relatedTransaction(sendTx)
			.build();

		pointTransactionRepository.save(sendTx);
		pointTransactionRepository.save(receiveTx);

		sender.getPointTransactions().add(sendTx);
		receiver.getPointTransactions().add(receiveTx);
	}

	// ✅ 거래 취소 시 보류 포인트 복구
	@Transactional
	public void cancelHolding(Long userId, int amount, String reason) {
		PointAccount account = pointAccountRepository.findByUserIdForUpdate(userId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다."));

		account.releaseHolding(amount);

		PointTransaction cancelTx = PointTransaction.builder()
			.account(account)
			.amount(amount)
			.transactionReason("Cancel: " + reason)
			.build();

		pointTransactionRepository.save(cancelTx);
		account.getPointTransactions().add(cancelTx);
	}

	// ✅ 즉시 송금 처리: 보류 없이 바로 이동 (ex. 선물하기, 즉시 결제 등)
	@Transactional
	public void transferPoints(PointTransferCommand command) {
		Timer.Sample sample = Timer.start(meterRegistry); // ⏱ 시작

		try {
			Long senderUserId = command.getSenderUserId();
			Long receiverUserId = command.getReceiverUserId();
			int amount = command.getAmount();
			String reason = command.getReason();

			if (amount <= 0) {
				meterRegistry.counter("point.transfer.failure", "reason", "amount_le_zero").increment();
				throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
			}

			Long firstUserId = Math.min(senderUserId, receiverUserId);
			Long secondUserId = Math.max(senderUserId, receiverUserId);

			PointAccount firstAccount = pointAccountRepository.findByUserIdForUpdate(firstUserId)
				.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + firstUserId));
			PointAccount secondAccount = pointAccountRepository.findByUserIdForUpdate(secondUserId)
				.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + secondUserId));

			PointAccount sender = senderUserId.equals(firstUserId) ? firstAccount : secondAccount;
			PointAccount receiver = receiverUserId.equals(firstUserId) ? firstAccount : secondAccount;

			if (sender.getAvailablePoints() < amount) {
				meterRegistry.counter("point.transfer.failure", "reason", "insufficient").increment();
				throw new IllegalArgumentException("송신자의 사용 가능 포인트가 부족합니다.");
			}

			sender.setAvailablePoints(sender.getAvailablePoints() - amount);
			receiver.setAvailablePoints(receiver.getAvailablePoints() + amount);

			PointTransaction sendTx = PointTransaction.builder()
				.account(sender)
				.amount(-amount)
				.transactionReason("Send: " + reason)
				.build();

			PointTransaction receiveTx = PointTransaction.builder()
				.account(receiver)
				.amount(amount)
				.transactionReason("Receive: " + reason)
				.relatedTransaction(sendTx)
				.build();

			pointTransactionRepository.save(sendTx);
			pointTransactionRepository.save(receiveTx);

			sender.getPointTransactions().add(sendTx);
			receiver.getPointTransactions().add(receiveTx);

			meterRegistry.counter("point.transfer.success", "topic", "p1").increment(); // ✅ 성공 카운터

		} catch (Exception e) {
			meterRegistry.counter("point.transfer.failure", "reason", "exception").increment(); // ✅ 실패 카운터
			throw e;

		} finally {
			sample.stop(meterRegistry.timer("point.transfer.latency", "topic", "p1")); // ✅ 처리 시간 기록
		}
	}

	// ✅ 즉시 송금 처리: 보류 없이 바로 이동 (ex. 선물하기, 즉시 결제 등)
	@Transactional
	public void transferPointsP4(PointTransferCommand command) {
		Timer.Sample sample = Timer.start(meterRegistry); // ⏱ 시작

		try {
			Long senderUserId = command.getSenderUserId();
			Long receiverUserId = command.getReceiverUserId();
			int amount = command.getAmount();
			String reason = command.getReason();

			if (amount <= 0) {
				meterRegistry.counter("point.transfer.failure", "reason", "amount_le_zero").increment();
				throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
			}

			Long firstUserId = Math.min(senderUserId, receiverUserId);
			Long secondUserId = Math.max(senderUserId, receiverUserId);

			PointAccount firstAccount = pointAccountRepository.findByUserIdForUpdate(firstUserId)
				.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + firstUserId));
			PointAccount secondAccount = pointAccountRepository.findByUserIdForUpdate(secondUserId)
				.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + secondUserId));

			PointAccount sender = senderUserId.equals(firstUserId) ? firstAccount : secondAccount;
			PointAccount receiver = receiverUserId.equals(firstUserId) ? firstAccount : secondAccount;

			if (sender.getAvailablePoints() < amount) {
				meterRegistry.counter("point.transfer.failure", "reason", "insufficient").increment();
				throw new IllegalArgumentException("송신자의 사용 가능 포인트가 부족합니다.");
			}

			sender.setAvailablePoints(sender.getAvailablePoints() - amount);
			receiver.setAvailablePoints(receiver.getAvailablePoints() + amount);

			PointTransaction sendTx = PointTransaction.builder()
				.account(sender)
				.amount(-amount)
				.transactionReason("Send: " + reason)
				.build();

			PointTransaction receiveTx = PointTransaction.builder()
				.account(receiver)
				.amount(amount)
				.transactionReason("Receive: " + reason)
				.relatedTransaction(sendTx)
				.build();

			pointTransactionRepository.save(sendTx);
			pointTransactionRepository.save(receiveTx);

			sender.getPointTransactions().add(sendTx);
			receiver.getPointTransactions().add(receiveTx);

			meterRegistry.counter("point.transfer.success", "topic", "p4").increment(); // ✅ 성공 카운터

		} catch (Exception e) {
			meterRegistry.counter("point.transfer.failure", "reason", "exception").increment(); // ✅ 실패 카운터
			throw e;

		} finally {
			sample.stop(meterRegistry.timer("point.transfer.latency", "topic", "p4")); // ✅ 처리 시간 기록
		}
	}


}

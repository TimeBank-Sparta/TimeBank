package com.timebank.pointservice.application.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.entity.PointTransaction;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointAccountRepository pointAccountRepository;
	private final PointTransactionRepository pointTransactionRepository;

	// 계정 생성
	public PointAccount createAccount(Long userId) {
		PointAccount newAccount = PointAccount.builder()
			.userId(userId)
			.totalPoints(600)
			.build();

		return pointAccountRepository.save(newAccount);
	}

	// 계정 조회
	public PointAccount getAccount(Long accountId) {
		return pointAccountRepository.findById(accountId)
			.orElseThrow(() -> new IllegalArgumentException("해당 계정을 찾을 수 없습니다."));
	}

	@Transactional
	public void transferPoints(PointTransferCommand command) {
		Long senderUserId = command.getSenderUserId();
		Long receiverUserId = command.getReceiverUserId();
		int amount = command.getAmount();
		String reason = command.getReason();

		if (amount <= 0) {
			throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
		}

		// ✅ 항상 userId 오름차순으로 락 획득
		Long firstUserId = Math.min(senderUserId, receiverUserId);
		Long secondUserId = Math.max(senderUserId, receiverUserId);

		PointAccount firstAccount = pointAccountRepository.findByUserIdForUpdate(firstUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + firstUserId));

		PointAccount secondAccount = pointAccountRepository.findByUserIdForUpdate(secondUserId)
			.orElseThrow(() -> new NoSuchElementException("계좌를 찾을 수 없습니다. userId=" + secondUserId));

		// 🔁 원래 송수신자와 매칭
		PointAccount sender = senderUserId.equals(firstUserId) ? firstAccount : secondAccount;
		PointAccount receiver = receiverUserId.equals(firstUserId) ? firstAccount : secondAccount;

		if (sender.getTotalPoints() < amount) {
			throw new IllegalArgumentException("송신자의 포인트가 부족합니다.");
		}

		// 💳 포인트 이체
		sender.setTotalPoints(sender.getTotalPoints() - amount);
		receiver.setTotalPoints(receiver.getTotalPoints() + amount);

		// 🧾 거래 내역 생성
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

		// 👉 연관 관계 추가
		sender.getPointTransactions().add(sendTx);
		receiver.getPointTransactions().add(receiveTx);
	}
}

package com.timebank.pointservice.application.service;

import org.springframework.stereotype.Service;

import com.timebank.pointservice.application.dto.PointTransferCommand;
import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.entity.PointTransaction;
import com.timebank.pointservice.domain.repository.PointAccountRepository;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;

import jakarta.transaction.Transactional;
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
		Long senderId = command.getSenderAccountId();
		Long receiverId = command.getReceiverAccountId();
		int amount = command.getAmount();
		String reason = command.getReason();

		if (amount <= 0) {
			throw new IllegalArgumentException("보낼 포인트는 1 이상이어야 합니다.");
		}

		PointAccount sender = pointAccountRepository.findById(senderId)
			.orElseThrow(() -> new IllegalArgumentException("송신자 계정을 찾을 수 없습니다."));

		PointAccount receiver = pointAccountRepository.findById(receiverId)
			.orElseThrow(() -> new IllegalArgumentException("수신자 계정을 찾을 수 없습니다."));

		if (sender.getTotalPoints() < amount) {
			throw new IllegalArgumentException("송신자의 포인트가 부족합니다.");
		}

		// 포인트 이동
		sender.setTotalPoints(sender.getTotalPoints() - amount);
		receiver.setTotalPoints(receiver.getTotalPoints() + amount);

		// 거래 내역 생성
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

		// sender, receiver 는 영속 상태이므로 pointTransactions 컬렉션에 수동 추가 가능
		sender.getPointTransactions().add(sendTx);
		receiver.getPointTransactions().add(receiveTx);
	}

}

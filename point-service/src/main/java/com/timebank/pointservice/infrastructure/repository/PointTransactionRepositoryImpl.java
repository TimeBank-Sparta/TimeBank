package com.timebank.pointservice.infrastructure.repository;

import org.springframework.stereotype.Repository;

import com.timebank.pointservice.domain.entity.PointTransaction;
import com.timebank.pointservice.domain.repository.PointTransactionRepository;

@Repository
public class PointTransactionRepositoryImpl implements PointTransactionRepository {
	private final PointTransactionJpaRepository pointTransactionJpaRepository;

	public PointTransactionRepositoryImpl(PointTransactionJpaRepository pointTransactionJpaRepository) {
		this.pointTransactionJpaRepository = pointTransactionJpaRepository;
	}

	@Override
	public PointTransaction save(PointTransaction pointTransaction) {
		return pointTransactionJpaRepository.save(pointTransaction);
	}
}
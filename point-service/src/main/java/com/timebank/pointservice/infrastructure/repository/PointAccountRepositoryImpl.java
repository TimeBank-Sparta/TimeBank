package com.timebank.pointservice.infrastructure.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.pointservice.domain.entity.PointAccount;
import com.timebank.pointservice.domain.repository.PointAccountRepository;

@Repository
public class PointAccountRepositoryImpl implements PointAccountRepository {
	private final PointAccountJpaRepository pointAccountJpaRepository;

	public PointAccountRepositoryImpl(PointAccountJpaRepository pointAccountJpaRepository) {
		this.pointAccountJpaRepository = pointAccountJpaRepository;
	}

	@Override
	public PointAccount save(PointAccount pointAccount) {
		return pointAccountJpaRepository.save(pointAccount);
	}

	@Override
	public Optional<PointAccount> findById(Long accountId) {
		return pointAccountJpaRepository.findById(accountId);
	}



}

package com.timebank.pointservice.domain.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.pointservice.domain.entity.PointAccount;

@Repository
public interface PointAccountRepository {
	PointAccount save(PointAccount pointAccount);

	Optional<PointAccount> findById(Long accountId);

	Optional<PointAccount> findByUserId(Long userId);

	Optional<PointAccount> findByUserIdForUpdate(Long userId); // 비관적 락 메서드


	void deleteAll();

}

package com.timebank.pointservice.domain.repository;

import org.springframework.stereotype.Repository;

import com.timebank.pointservice.domain.entity.PointTransaction;

@Repository
public interface PointTransactionRepository {
	PointTransaction save(PointTransaction pointTransaction);
}

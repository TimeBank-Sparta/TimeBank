package com.timebank.pointservice.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timebank.pointservice.domain.entity.PointTransaction;

@Repository
public interface PointTransactionJpaRepository extends JpaRepository<PointTransaction, Long> {
}
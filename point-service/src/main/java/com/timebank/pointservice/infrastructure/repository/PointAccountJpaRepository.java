package com.timebank.pointservice.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.timebank.pointservice.domain.entity.PointAccount;

import jakarta.persistence.LockModeType;

@Repository
public interface PointAccountJpaRepository extends JpaRepository<PointAccount, Long> {

}

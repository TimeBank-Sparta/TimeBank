package com.timebank.userservice.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.domain.repository.user.UserRepository;

import jakarta.persistence.LockModeType;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
//	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUsername(String username);

}

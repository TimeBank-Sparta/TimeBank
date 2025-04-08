package com.timebank.userservice.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.user.domain.model.User;
import com.timebank.userservice.user.domain.repository.UserRepository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

	Optional<User> findByUsername(String username);
}

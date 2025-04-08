package com.timebank.userservice.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.user.User;
import com.timebank.userservice.domain.repository.user.UserRepository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

	Optional<User> findByUsername(String username);
}

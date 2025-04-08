package com.timebank.userservice.user.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.userservice.user.domain.model.User;

@Repository
public interface UserRepository {
	List<User> findAll();

	User save(User user);

	Optional<User> findByUsername(String username);
}

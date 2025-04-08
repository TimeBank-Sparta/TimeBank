package com.timebank.userservice.domain.repository.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.user.User;

@Repository
public interface UserRepository {
	List<User> findAll();

	User save(User user);

	Optional<User> findByUsername(String username);

	Optional<User> findById(Long id);
}

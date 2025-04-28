package com.timebank.userservice.domain.repository.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.user.User;

@Repository
public interface UserRepository {

	Optional<User> findByUsername(String username);

}

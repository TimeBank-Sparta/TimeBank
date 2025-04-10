package com.timebank.userservice.domain.repository.profile;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.profile.UserProfile;

@Repository
public interface UserProfileRepository {
	UserProfile save(UserProfile userProfile);

	Optional<UserProfile> findByNickname(String nickname);

	Optional<UserProfile> findById(Long id);

	Optional<UserProfile> findByUserId(Long userId);
}

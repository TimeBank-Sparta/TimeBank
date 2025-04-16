package com.timebank.userservice.domain.repository.profile;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.profile.UserProfile;

@Repository
public interface UserProfileRepository {

	Optional<UserProfile> findByNickname(String nickname);

	Optional<UserProfile> findByUserId(Long userId);

	List<UserProfile> findAllByUserIdIn(List<Long> userIdList);
}

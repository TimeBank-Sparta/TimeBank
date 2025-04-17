package com.timebank.userservice.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.profile.UserProfile;
import com.timebank.userservice.domain.repository.profile.UserProfileRepository;

@Repository
public interface JpaUserProfileRepository extends JpaRepository<UserProfile, Long>, UserProfileRepository {

	Optional<UserProfile> findByNickname(String nickname);

	Optional<UserProfile> findByUserId(Long userId);

	List<UserProfile> findAllByUserIdIn(List<Long> userIdList);
}

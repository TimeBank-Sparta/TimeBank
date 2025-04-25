package com.timebank.userservice.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.timebank.userservice.domain.model.profile.UserProfile;
import com.timebank.userservice.domain.repository.profile.UserProfileRepository;

@Repository
public interface JpaUserProfileRepository extends JpaRepository<UserProfile, Long>, UserProfileRepository {

	Optional<UserProfile> findByNickname(String nickname);

	Optional<UserProfile> findByUserId(Long userId);

	@Query("""
    SELECT p FROM UserProfile p
    LEFT JOIN FETCH p.helpServices
    LEFT JOIN FETCH p.needServices
    WHERE p.user.id = :userId
""")
	Optional<UserProfile> findWithServicesByUserId(@Param("userId") Long userId);

	List<UserProfile> findAllByUser_IdIn(List<Long> userIdList);
}

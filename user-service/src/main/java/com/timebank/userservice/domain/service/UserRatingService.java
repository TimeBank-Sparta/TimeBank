package com.timebank.userservice.domain.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timebank.userservice.domain.model.profile.UserProfile;
import com.timebank.userservice.domain.repository.profile.UserProfileRepository;
import com.timebank.userservice.infrastructure.client.ReviewClient2;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserRatingService {
	private final ReviewClient2 reviewClient;
	private final UserProfileRepository userProfileRepository;

	public void updateAverageRating(Long userId) {
		List<Byte> ratings = reviewClient.getRatingsByUserId(userId);
		double avg = ratings.stream()
			.mapToInt(Byte::intValue)
			.average()
			.orElse(0.0);

		UserProfile profile = userProfileRepository.findByUserId(userId)
			.orElseThrow(() -> new EntityNotFoundException("UserProfile not found"));

		profile.updateRating(avg);
		userProfileRepository.save(profile);
	}
}

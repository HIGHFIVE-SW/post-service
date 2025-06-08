package com.trendist.post_service.domain.review.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.dto.response.ReviewGetKeywordCountResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetTypeCountResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetUserResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewMonthlyCountResponse;
import com.trendist.post_service.domain.review.repository.ReviewRepository;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewProfileService {
	private final ReviewRepository reviewRepository;
	private final UserServiceClient userServiceClient;

	public Page<ReviewGetUserResponse> getMyReviews(int page) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

		return reviewRepository.findByUserIdAndDeletedFalse(userId, pageable)
			.map(ReviewGetUserResponse::from);
	}

	public Page<ReviewGetUserResponse> getUserReviews(int page, UUID userId) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

		return reviewRepository.findByUserIdAndDeletedFalse(userId, pageable)
			.map(ReviewGetUserResponse::from);
	}

	public List<ReviewGetTypeCountResponse> countMyReviewsByType() {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		return countByType(userId);
	}

	public List<ReviewGetTypeCountResponse> countUserReviewsByType(UUID userId) {
		return countByType(userId);
	}

	public List<ReviewGetKeywordCountResponse> countMyReviewsByKeyword() {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		return countByKeyword(userId);
	}

	public List<ReviewGetKeywordCountResponse> countUserReviewsByKeyword(UUID userId) {
		return countByKeyword(userId);
	}

	public List<ReviewMonthlyCountResponse> countMyReviewsByMonth() {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		return reviewRepository.countMonthlyByUserIdByEndDate(userId);
	}

	public List<ReviewMonthlyCountResponse> countUserReviewsByMonth(UUID userId) {
		return reviewRepository.countMonthlyByUserIdByEndDate(userId);
	}

	private List<ReviewGetTypeCountResponse> countByType(UUID userId) {
		Map<ActivityType, Long> counts = reviewRepository.findAllByUserIdAndDeletedFalse(userId)
			.stream()
			.collect(Collectors.groupingBy(Review::getActivityType, Collectors.counting()));

		return Arrays.stream(ActivityType.values())
			.map(type -> ReviewGetTypeCountResponse.builder()
				.userId(userId)
				.activityType(type)
				.count(counts.getOrDefault(type, 0L))
				.build()
			)
			.toList();
	}

	private List<ReviewGetKeywordCountResponse> countByKeyword(UUID userId) {
		Map<Keyword, Long> counts = reviewRepository.findAllByUserIdAndDeletedFalse(userId)
			.stream()
			.collect(Collectors.groupingBy(Review::getKeyword, Collectors.counting()));

		return Arrays.stream(Keyword.values())
			.map(keyword -> ReviewGetKeywordCountResponse.builder()
				.userId(userId)
				.keyword(keyword)
				.count(counts.getOrDefault(keyword, 0L))
				.build()
			)
			.toList();
	}
}

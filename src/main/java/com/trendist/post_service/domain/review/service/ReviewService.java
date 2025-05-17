package com.trendist.post_service.domain.review.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.domain.ReviewLike;
import com.trendist.post_service.domain.review.domain.ReviewSort;
import com.trendist.post_service.domain.review.dto.request.ReviewCreateRequest;
import com.trendist.post_service.domain.review.dto.request.ReviewUpdateRequest;
import com.trendist.post_service.domain.review.dto.response.ReviewCreateResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewDeleteResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetAllResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewLikeResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewUpdateResponse;
import com.trendist.post_service.domain.review.repository.ReviewLikeRepository;
import com.trendist.post_service.domain.review.repository.ReviewRepository;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.global.response.status.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	private final UserServiceClient userServiceClient;

	@Transactional
	public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		String nickname = userServiceClient.getMyProfile("").getResult().nickname();

		//review 작성 시 이미지 최소 1장 필수
		if (reviewCreateRequest.imageUrls() == null || reviewCreateRequest.imageUrls().isEmpty()) {
			throw new ApiException(ErrorStatus._REVIEW_IMAGE_REQUIRED);
		}

		Review review = Review.builder()
			.title(reviewCreateRequest.title())
			.keyword(reviewCreateRequest.keyword())
			.activityType(reviewCreateRequest.activityType())
			.activityPeriod(reviewCreateRequest.activityPeriod())
			.activityEndDate(reviewCreateRequest.activityEndDate())
			.activityName(reviewCreateRequest.activityName())
			.content(reviewCreateRequest.content())
			.awardImageUrl(reviewCreateRequest.awardImageUrl())
			.imageUrls(reviewCreateRequest.imageUrls())
			.userId(userId)
			.nickname(nickname)
			.build();

		reviewRepository.save(review);

		return ReviewCreateResponse.from(review);
	}

	@Transactional
	public ReviewUpdateResponse updateReview(UUID reviewId, ReviewUpdateRequest reviewUpdateRequest) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));

		UUID nowUserId = userServiceClient.getMyProfile("").getResult().id();
		if (!nowUserId.equals(review.getUserId())) {
			throw new ApiException(ErrorStatus._REVIEW_UPDATE_FORBIDDEN);
		}

		//review 작성 시 이미지 최소 1장 필수
		if (reviewUpdateRequest.imageUrls() == null || reviewUpdateRequest.imageUrls().isEmpty()) {
			throw new ApiException(ErrorStatus._REVIEW_IMAGE_REQUIRED);
		}

		review.setTitle(reviewUpdateRequest.title());
		review.setContent(reviewUpdateRequest.content());
		review.setAwardImageUrl(reviewUpdateRequest.awardImageUrl());
		review.setImageUrls(reviewUpdateRequest.imageUrls());

		reviewRepository.save(review);

		return ReviewUpdateResponse.from(review);
	}

	@Transactional
	public ReviewDeleteResponse deleteReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));

		UUID nowUserId = userServiceClient.getMyProfile("").getResult().id();
		if (!nowUserId.equals(review.getUserId())) {
			throw new ApiException(ErrorStatus._REVIEW_DELETE_FORBIDDEN);
		}

		review.setDeleted(true);

		reviewRepository.save(review);

		return ReviewDeleteResponse.from(review);
	}

	public Page<ReviewGetAllResponse> getReviews(
		Keyword keyword,
		ActivityType activityType,
		ReviewSort reviewSort,
		int page) {
		Sort sort = switch (reviewSort) {
			case LIKES -> Sort.by("likeCount").descending();
			case RECENT -> Sort.by("createdAt").descending();
		};

		Pageable pageable = PageRequest.of(page, 9, sort);

		if (keyword != null && activityType != null) {
			return reviewRepository.findAllByKeywordAndActivityTypeAndDeletedFalse(keyword, activityType, pageable)
				.map(ReviewGetAllResponse::from);
		} else if (keyword != null) {
			return reviewRepository.findAllByKeywordAndDeletedFalse(keyword, pageable)
				.map(ReviewGetAllResponse::from);
		} else if (activityType != null) {
			return reviewRepository.findAllByActivityTypeAndDeletedFalse(activityType, pageable)
				.map(ReviewGetAllResponse::from);
		} else {
			return reviewRepository
				.findAllByDeletedFalse(pageable)
				.map(ReviewGetAllResponse::from);
		}
	}

	public ReviewGetResponse getReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));
		String profileUrl = userServiceClient.getUserProfile(review.getUserId()).getResult().profileUrl();

		return ReviewGetResponse.of(review, profileUrl);
	}

	@Transactional
	public ReviewLikeResponse likeReview(UUID reviewId) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		ReviewLike reviewLike;
		boolean like;

		if (!reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId)) {
			reviewLike = ReviewLike.builder()
				.reviewId(reviewId)
				.userId(userId)
				.build();
			reviewLikeRepository.save(reviewLike);
			reviewRepository.incrementLikeCount(reviewId);
			like = true;
		} else {
			reviewLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId)
				.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_LIKE_NOT_FOUND));
			reviewLikeRepository.deleteByReviewIdAndUserId(reviewId, userId);
			reviewRepository.decrementLikeCount(reviewId);
			like = false;
		}

		return ReviewLikeResponse.of(reviewLike, like);
	}
}

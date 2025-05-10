package com.trendist.post_service.domain.review.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.dto.request.ReviewCreateRequest;
import com.trendist.post_service.domain.review.dto.request.ReviewUpdateRequest;
import com.trendist.post_service.domain.review.dto.response.ReviewCreateResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewDeleteResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetAllResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetMineResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewUpdateResponse;
import com.trendist.post_service.domain.review.repository.ReviewRepository;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.global.response.status.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final UserServiceClient userServiceClient;

	@Transactional
	public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		String nickname = userServiceClient.getMyProfile("").getResult().nickname();

		//review 작성 시 이미지 최소 1장 필수
		if (reviewCreateRequest.imageUrls().isEmpty()) {
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
		if (reviewUpdateRequest.imageUrls().isEmpty()) {
			throw new ApiException(ErrorStatus._REVIEW_IMAGE_REQUIRED);
		}

		review.setTitle(reviewUpdateRequest.title());
		review.setKeyword(reviewUpdateRequest.keyword());
		review.setActivityType(reviewUpdateRequest.activityType());
		review.setActivityPeriod(reviewUpdateRequest.activityPeriod());
		review.setActivityEndDate(reviewUpdateRequest.activityEndDate());
		review.setActivityName(reviewUpdateRequest.activityName());
		review.setContent(reviewUpdateRequest.content());
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

	public Page<ReviewGetAllResponse> getAllReviews(int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
		return reviewRepository.findAllByDeletedFalse(pageable)
			.map(ReviewGetAllResponse::from);
	}

	public ReviewGetResponse getReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));
		String profileUrl = userServiceClient.getUserProfile(review.getUserId()).getResult().profileUrl();

		return ReviewGetResponse.of(review, profileUrl);
	}

	public Page<ReviewGetMineResponse> getMyReviews(int page) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

		return reviewRepository.findByUserIdAndDeletedFalse(userId, pageable)
			.map(ReviewGetMineResponse::from);
	}
}

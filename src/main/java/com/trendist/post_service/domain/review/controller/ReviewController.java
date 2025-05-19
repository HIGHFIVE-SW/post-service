package com.trendist.post_service.domain.review.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.ReviewSort;
import com.trendist.post_service.domain.review.dto.request.ReviewActivityCreateRequest;
import com.trendist.post_service.domain.review.dto.request.ReviewCreateRequest;
import com.trendist.post_service.domain.review.dto.request.ReviewUpdateRequest;
import com.trendist.post_service.domain.review.dto.response.ReviewCreateResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewDeleteResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetAllResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewLikeResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewSearchResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewUpdateResponse;
import com.trendist.post_service.domain.review.service.ReviewService;
import com.trendist.post_service.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	@Operation(
		summary = "리뷰 게시판 게시물 생성",
		description = "사용자가 리뷰 게시판에 게시불을 생성합니다."
	)
	@PostMapping
	public ApiResponse<ReviewCreateResponse> createReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
		return ApiResponse.onSuccess(reviewService.createReview(reviewCreateRequest));
	}

	@Operation(
		summary = "특정 활동 리뷰 생성",
		description = "사용자가 특정 활동에 대한 리뷰 게시판에 게시불을 생성합니다."
	)
	@PostMapping("{activityId}")
	public ApiResponse<ReviewCreateResponse> createActivityReview(
		@PathVariable(name = "activityId") UUID activityId,
		@RequestBody ReviewActivityCreateRequest request
	) {
		return ApiResponse.onSuccess(reviewService.createActivityReview(activityId, request));
	}

	@Operation(
		summary = "리뷰 게시판 게시물 수정",
		description = "사용자가 리뷰 게시판에 게시물을 수정합니다."
	)
	@PatchMapping("/update/{reviewId}")
	public ApiResponse<ReviewUpdateResponse> updateReview(
		@PathVariable(name = "reviewId") UUID reviewId,
		@RequestBody ReviewUpdateRequest reviewUpdateRequest) {
		return ApiResponse.onSuccess(reviewService.updateReview(reviewId, reviewUpdateRequest));
	}

	@Operation(
		summary = "리뷰 게시판 게시물 삭제",
		description = "사용자가 리뷰 게시판에 게시물을 삭제합니다."
	)
	@PatchMapping("/delete/{reviewId}")
	public ApiResponse<ReviewDeleteResponse> deleteReview(@PathVariable(name = "reviewId") UUID reviewId) {
		return ApiResponse.onSuccess(reviewService.deleteReview(reviewId));
	}

	@Operation(
		summary = "리뷰 게시판 조회",
		description = "리뷰 게시판의 게시물을 특정 조건에 따라 조회합니다."
	)
	@GetMapping
	public ApiResponse<Page<ReviewGetAllResponse>> getReviews(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(required = false) Keyword keyword,
		@RequestParam(required = false) ActivityType activityType,
		@RequestParam(name = "sort", defaultValue = "RECENT") ReviewSort sort
	) {
		return ApiResponse.onSuccess(reviewService.getReviews(keyword, activityType, sort, page));
	}

	@Operation(
		summary = "리뷰 게시판 특정 게시물 조회",
		description = "사용자가 리뷰 게시판에 있는 특정 게시물을 상세 조회합니다."
	)
	@GetMapping("/{reviewId}")
	public ApiResponse<ReviewGetResponse> getReview(@PathVariable(name = "reviewId") UUID reviewId) {
		return ApiResponse.onSuccess(reviewService.getReview(reviewId));
	}

	@Operation(
		summary = "리뷰 게시물 좋아요",
		description = "사용자가 리뷰 게시판에 있는 특정 게시물에 좋아요를 누르거나 취소합니다."
	)
	@PostMapping("/{reviewId}/like")
	public ApiResponse<ReviewLikeResponse> likeReview(@PathVariable(name = "reviewId") UUID reviewId) {
		return ApiResponse.onSuccess(reviewService.likeReview(reviewId));
	}

	@Operation(
		summary = "리뷰 게시판 검색",
		description = "사용자가 리뷰 게시판에 특정 단어를 입력하여 해당하는 리뷰를 검색합니다"
	)
	@GetMapping("/search")
	public ApiResponse<Page<ReviewSearchResponse>> searchReviews(
		@RequestParam String keyword,
		@RequestParam(defaultValue = "0") int page
	) {
		return ApiResponse.onSuccess(reviewService.searchReviews(keyword, page));
	}
}

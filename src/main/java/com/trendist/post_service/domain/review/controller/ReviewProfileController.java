package com.trendist.post_service.domain.review.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.domain.review.dto.response.ReviewGetKeywordCountResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetTypeCountResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetUserResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewMonthlyCountResponse;
import com.trendist.post_service.domain.review.service.ReviewProfileService;
import com.trendist.post_service.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/reviews")
public class ReviewProfileController {
	private final ReviewProfileService reviewProfileService;

	@Operation(
		summary = "내가 쓴 리뷰 게시판 게시물 조회",
		description = "현재 로그인한 사용자가 리뷰 게시판에 자신이 생성한 게시물들을 조회합니다.(본인 프로필용)"
	)
	@GetMapping("/mine")
	public ApiResponse<Page<ReviewGetUserResponse>> getMyReviews(@RequestParam(defaultValue = "0") int page) {
		return ApiResponse.onSuccess(reviewProfileService.getMyReviews(page));
	}

	@Operation(
		summary = "특정 사용자가 쓴 리뷰 게시물 조회",
		description = "특정 사용자가 리뷰 게시판에 본인이 생성한 게시물들을 조회합니다.(특정 사용자 프로필용)"
	)
	@GetMapping("/{userId}")
	public ApiResponse<Page<ReviewGetUserResponse>> getUserReviews(
		@RequestParam(defaultValue = "0") int page,
		@PathVariable(name = "userId") UUID userId) {
		return ApiResponse.onSuccess(reviewProfileService.getUserReviews(page, userId));
	}

	@Operation(
		summary = "활동 종류별 자신이 진행한 활동 통계 조회",
		description = "활동 종류별로 자신이 진행한 활동들이 총 몇개인지 통계를 조회합니다."
	)
	@GetMapping("/mine/type/count")
	public ApiResponse<List<ReviewGetTypeCountResponse>> countMyReviewsByType() {
		return ApiResponse.onSuccess(reviewProfileService.countMyReviewsByType());
	}

	@Operation(
		summary = "활동 종류별 특정 사용자가 진행한 활동 통계 조회",
		description = "활동 종류별로 특정 사용자가 진행한 활동들이 총 몇개인지 통계를 조회합니다."
	)
	@GetMapping("/{userId}/type/count")
	public ApiResponse<List<ReviewGetTypeCountResponse>> countUserReviewsByType(
		@PathVariable(name = "userId") UUID userId) {
		return ApiResponse.onSuccess(reviewProfileService.countUserReviewsByType(userId));
	}

	@Operation(
		summary = "키워드별 자신이 진행한 활동 통계 조회",
		description = "키워드별로 자신이 진행한 활동들이 총 몇개인지 통계를 조회합니다."
	)
	@GetMapping("/mine/keyword/count")
	public ApiResponse<List<ReviewGetKeywordCountResponse>> countMyReviewsByKeyword() {
		return ApiResponse.onSuccess(reviewProfileService.countMyReviewsByKeyword());
	}

	@Operation(
		summary = "키워드별 특정 사용자가 진행한 활동 통계 조회",
		description = "키워드별로 특정 사용자가 진행한 활동들이 총 몇개인지 통계를 조회합니다."
	)
	@GetMapping("/{userId}/keyword/count")
	public ApiResponse<List<ReviewGetKeywordCountResponse>> countUserReviewsByKeyword(
		@PathVariable(name = "userId") UUID userId) {
		return ApiResponse.onSuccess(reviewProfileService.countUserReviewsByKeyword(userId));
	}

	@Operation(
		summary = "월별 자신이 진행한 활동 수 조회",
		description = "월별로 자신이 진행한 활동 수가 몇개인지 통계를 조회합니다."
	)
	@GetMapping("/mine/month/count")
	public ApiResponse<List<ReviewMonthlyCountResponse>> countMyReviewsByMonth() {
		return ApiResponse.onSuccess(reviewProfileService.countMyReviewsByMonth());
	}

	@Operation(
		summary = "월별 자신이 진행한 활동 수 조회",
		description = "월별로 자신이 진행한 활동 수가 몇개인지 통계를 조회합니다."
	)
	@GetMapping("/{userId}/month/count")
	public ApiResponse<List<ReviewMonthlyCountResponse>> countUserReviewsByMonth(
		@PathVariable(name = "userId") UUID userId
	) {
		return ApiResponse.onSuccess(reviewProfileService.countUserReviewsByMonth(userId));
	}
}

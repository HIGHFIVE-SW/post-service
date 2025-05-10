package com.trendist.post_service.domain.review.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ReviewLike;

import lombok.Builder;

@Builder
public record ReviewLikeResponse(
	UUID likeId,
	UUID reviewId,
	UUID userId,
	Boolean like
) {
	public static ReviewLikeResponse of(ReviewLike reviewLike, Boolean like) {
		return ReviewLikeResponse.builder()
			.likeId(reviewLike.getId())
			.reviewId(reviewLike.getReviewId())
			.userId(reviewLike.getUserId())
			.like(like)
			.build();
	}
}

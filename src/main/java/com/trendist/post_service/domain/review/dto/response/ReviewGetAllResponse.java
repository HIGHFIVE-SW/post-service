package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetAllResponse(
	UUID id,
	String title,
	String nickname,
	Integer likeCount,
	LocalDateTime createAt
) {
	public static ReviewGetAllResponse from(Review review) {
		return ReviewGetAllResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.nickname(review.getNickname())
			.likeCount(review.getLikeCount())
			.createAt(review.getCreatedAt())
			.build();
	}
}

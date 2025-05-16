package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetUserResponse(
	UUID id,
	String title,
	LocalDateTime updatedAt
) {
	public static ReviewGetUserResponse from(Review review) {
		return ReviewGetUserResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.updatedAt(review.getUpdatedAt())
			.build();
	}
}

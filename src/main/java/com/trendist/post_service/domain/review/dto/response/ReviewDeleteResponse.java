package com.trendist.post_service.domain.review.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewDeleteResponse(
	UUID id,
	String title,
	Boolean deleted
) {
	public static ReviewDeleteResponse from(Review review) {
		return ReviewDeleteResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.deleted(review.getDeleted())
			.build();
	}
}

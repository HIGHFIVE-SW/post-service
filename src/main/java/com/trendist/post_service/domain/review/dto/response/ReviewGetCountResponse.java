package com.trendist.post_service.domain.review.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetCountResponse(
	UUID userId,
	ActivityType activityType,
	Long count
) {
	public static ReviewGetCountResponse of(Review review, Long count) {
		return ReviewGetCountResponse.builder()
			.userId(review.getUserId())
			.activityType(review.getActivityType())
			.count(count)
			.build();
	}
}

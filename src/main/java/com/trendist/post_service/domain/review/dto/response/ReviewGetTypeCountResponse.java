package com.trendist.post_service.domain.review.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetTypeCountResponse(
	UUID userId,
	ActivityType activityType,
	Long count
) {
	public static ReviewGetTypeCountResponse of(Review review, Long count) {
		return ReviewGetTypeCountResponse.builder()
			.userId(review.getUserId())
			.activityType(review.getActivityType())
			.count(count)
			.build();
	}
}

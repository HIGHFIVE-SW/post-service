package com.trendist.post_service.domain.review.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetCountByType(
	UUID userId,
	ActivityType activityType,
	Long count
) {
	public static ReviewGetCountByType of(Review review, Long count) {
		return ReviewGetCountByType.builder()
			.userId(review.getUserId())
			.activityType(review.getActivityType())
			.count(count)
			.build();
	}
}

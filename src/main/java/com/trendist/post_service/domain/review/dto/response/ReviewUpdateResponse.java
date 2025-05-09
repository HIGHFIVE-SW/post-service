package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityPeriod;
import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewUpdateResponse(
	UUID id,
	String title,
	Keyword keyword,
	ActivityType activityType,
	ActivityPeriod activityPeriod,
	LocalDate activityEndDate,
	String activityName,
	String content,
	List<String> imageUrls,
	LocalDateTime updatedAt
) {
	public static ReviewUpdateResponse from(Review review) {
		return ReviewUpdateResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.keyword(review.getKeyword())
			.activityType(review.getActivityType())
			.activityPeriod(review.getActivityPeriod())
			.activityEndDate(review.getActivityEndDate())
			.activityName(review.getActivityName())
			.content(review.getContent())
			.imageUrls(review.getImageUrls())
			.updatedAt(review.getUpdatedAt())
			.build();
	}
}

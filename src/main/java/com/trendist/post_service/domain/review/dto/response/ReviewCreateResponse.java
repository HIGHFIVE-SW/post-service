package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityPeriod;
import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewCreateResponse(
	UUID id,
	String title,
	UUID userId,
	String nickname,
	Keyword keyword,
	ActivityType activityType,
	ActivityPeriod activityPeriod,
	LocalDate activityEndDate,
	String activityName,
	String content,
	List<String> imageUrls,
	Boolean deleted
) {
	public static ReviewCreateResponse from(Review review) {
		return ReviewCreateResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.userId(review.getUserId())
			.nickname(review.getNickname())
			.keyword(review.getKeyword())
			.activityType(review.getActivityType())
			.activityPeriod(review.getActivityPeriod())
			.activityEndDate(review.getActivityEndDate())
			.activityName(review.getActivityName())
			.content(review.getContent())
			.imageUrls(review.getImageUrls())
			.deleted(review.getDeleted())
			.build();
	}
}

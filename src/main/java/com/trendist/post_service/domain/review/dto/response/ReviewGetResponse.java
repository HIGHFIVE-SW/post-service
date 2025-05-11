package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityPeriod;
import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetResponse(
	UUID id,
	String title,
	Keyword keyword,
	ActivityType activityType,
	ActivityPeriod activityPeriod,
	LocalDate activityEndDate,
	String activityName,
	String content,
	Integer likeCount,
	UUID userId,
	String nickname,
	String profileUrl,
	LocalDateTime createdAt
) {
	public static ReviewGetResponse of(Review review, String profileUrl) {
		return ReviewGetResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.keyword(review.getKeyword())
			.activityType(review.getActivityType())
			.activityPeriod(review.getActivityPeriod())
			.activityEndDate(review.getActivityEndDate())
			.activityName(review.getActivityName())
			.content(review.getContent())
			.likeCount(review.getLikeCount())
			.userId(review.getUserId())
			.nickname(review.getNickname())
			.profileUrl(profileUrl)
			.createdAt(review.getCreatedAt())
			.build();
	}
}

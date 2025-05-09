package com.trendist.post_service.domain.review.dto.response;

import java.util.List;
import java.util.UUID;

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
			.content(review.getContent())
			.imageUrls(review.getImageUrls())
			.deleted(review.getDeleted())
			.build();
	}
}

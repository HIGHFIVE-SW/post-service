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
	UUID activityId,
	String title,
	UUID userId,
	String nickname,
	Keyword keyword,
	ActivityType activityType,
	ActivityPeriod activityPeriod,
	LocalDate activityEndDate,
	String activityName,
	String content,
	String awardImageUrl,
	List<String> imageUrls,
	Boolean award_ocr_result,
	Boolean ocr_result,
	Boolean deleted
) {
	public static ReviewCreateResponse from(Review review) {
		return ReviewCreateResponse.builder()
			.id(review.getId())
			.activityId(review.getActivityId())
			.title(review.getTitle())
			.userId(review.getUserId())
			.nickname(review.getNickname())
			.keyword(review.getKeyword())
			.activityType(review.getActivityType())
			.activityPeriod(review.getActivityPeriod())
			.activityEndDate(review.getActivityEndDate())
			.activityName(review.getActivityName())
			.content(review.getContent())
			.awardImageUrl(review.getAwardImageUrl())
			.imageUrls(review.getImageUrls())
			.award_ocr_result(review.getAwardOcrResult())
			.ocr_result(review.getOcrResult())
			.deleted(review.getDeleted())
			.build();
	}
}

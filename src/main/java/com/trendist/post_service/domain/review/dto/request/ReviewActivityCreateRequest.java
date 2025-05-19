package com.trendist.post_service.domain.review.dto.request;

import java.util.List;

import com.trendist.post_service.domain.review.domain.ActivityPeriod;

public record ReviewActivityCreateRequest(
	String title,
	ActivityPeriod activityPeriod,
	String content,
	String awardImageUrl,
	List<String> imageUrls
) {
}

package com.trendist.post_service.domain.review.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.trendist.post_service.domain.review.domain.ActivityPeriod;
import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;

public record ReviewUpdateRequest(
	String title,
	Keyword keyword,
	ActivityType activityType,
	ActivityPeriod activityPeriod,
	LocalDate activityEndDate,
	String activityName,
	String content,
	List<String> imageUrls
) {
}

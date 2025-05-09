package com.trendist.post_service.domain.review.dto.request;

import java.util.List;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;

public record ReviewUpdateRequest(
	String title,
	Keyword keyword,
	ActivityType activityType,
	String content,
	List<String> imageUrls
) {
}

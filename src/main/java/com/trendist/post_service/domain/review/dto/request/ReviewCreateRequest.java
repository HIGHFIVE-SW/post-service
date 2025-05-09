package com.trendist.post_service.domain.review.dto.request;

import java.util.List;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;

import lombok.Builder;

@Builder
public record ReviewCreateRequest(
	String title,
	Keyword keyword,
	ActivityType activityType,
	String content,
	List<String> imageUrls
) {
}

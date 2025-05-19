package com.trendist.post_service.global.feign.activity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;

import lombok.Builder;

@Builder
public record ActivityGetResponse(
	UUID id,
	String name,
	String siteUrl,
	Keyword keyword,
	ActivityType activityType,
	LocalDateTime startDate,
	LocalDateTime endDate,
	String imageUrl
) {
}

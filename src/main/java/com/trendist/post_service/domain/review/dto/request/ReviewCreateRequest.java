package com.trendist.post_service.domain.review.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record ReviewCreateRequest(
	String title,
	String content,
	List<String> imageUrls
) {
}

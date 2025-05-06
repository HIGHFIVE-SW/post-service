package com.trendist.post_service.domain.post.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record PostCreateRequest(
	String title,
	String content,
	List<String> imageUrls
) {
}

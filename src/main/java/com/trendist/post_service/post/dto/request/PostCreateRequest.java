package com.trendist.post_service.post.dto.request;

import java.util.List;
import java.util.Set;

import com.trendist.post_service.post.domain.Keyword;

import lombok.Builder;

@Builder
public record PostCreateRequest(
	String title,
	String content,
	Set<Keyword> keywords,
	List<String> imageUrls
) {
}

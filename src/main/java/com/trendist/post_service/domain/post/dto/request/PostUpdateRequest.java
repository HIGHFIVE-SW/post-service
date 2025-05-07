package com.trendist.post_service.domain.post.dto.request;

import java.util.List;

public record PostUpdateRequest(
	String title,
	String content,
	List<String> imageUrls
) {
}

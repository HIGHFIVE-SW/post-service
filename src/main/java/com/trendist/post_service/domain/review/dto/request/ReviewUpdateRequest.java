package com.trendist.post_service.domain.review.dto.request;

import java.util.List;

public record ReviewUpdateRequest(
	String title,
	String content,
	String awardImageUrl,
	List<String> imageUrls
) {
}

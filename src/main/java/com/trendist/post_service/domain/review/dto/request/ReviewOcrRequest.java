package com.trendist.post_service.domain.review.dto.request;

import java.util.List;

public record ReviewOcrRequest (
	String awardImageUrl,
	List<String> imageUrls
) {
}

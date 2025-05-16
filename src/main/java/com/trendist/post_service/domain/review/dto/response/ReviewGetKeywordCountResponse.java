package com.trendist.post_service.domain.review.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetKeywordCountResponse(
	UUID userId,
	Keyword keyword,
	Long count
) {
	public static ReviewGetKeywordCountResponse of(Review review, Long count) {
		return ReviewGetKeywordCountResponse.builder()
			.userId(review.getUserId())
			.keyword(review.getKeyword())
			.count(count)
			.build();
	}
}

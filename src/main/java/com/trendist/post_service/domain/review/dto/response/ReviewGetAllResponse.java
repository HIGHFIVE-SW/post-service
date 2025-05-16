package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewGetAllResponse(
	UUID id,
	String title,
	Keyword keyword,
	List<String> imageUrls,
	String nickname,
	String content,
	Integer likeCount,
	LocalDateTime createAt
) {
	public static ReviewGetAllResponse from(Review review) {
		return ReviewGetAllResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.keyword(review.getKeyword())
			.imageUrls(review.getImageUrls())
			.nickname(review.getNickname())
			.content(review.getContent())
			.likeCount(review.getLikeCount())
			.createAt(review.getCreatedAt())
			.build();
	}
}

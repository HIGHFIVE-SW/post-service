package com.trendist.post_service.domain.review.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.Review;

import lombok.Builder;

@Builder
public record ReviewUpdateResponse(
	UUID id,
	String title,
	String content,
	String awardImageUrl,
	List<String> imageUrls,
	LocalDateTime updatedAt,
	Boolean award_ocr_result,
	Boolean ocr_result
) {
	public static ReviewUpdateResponse from(Review review) {
		return ReviewUpdateResponse.builder()
			.id(review.getId())
			.title(review.getTitle())
			.content(review.getContent())
			.awardImageUrl(review.getAwardImageUrl())
			.imageUrls(review.getImageUrls())
			.updatedAt(review.getUpdatedAt())
			.award_ocr_result(review.getAwardOcrResult())
			.ocr_result(review.getOcrResult())
			.build();
	}
}

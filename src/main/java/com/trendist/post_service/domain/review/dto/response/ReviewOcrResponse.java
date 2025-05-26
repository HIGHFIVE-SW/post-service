package com.trendist.post_service.domain.review.dto.response;

public record ReviewOcrResponse(
	Boolean awardOcrResult,
	Boolean ocrResult
) {
}
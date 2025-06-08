package com.trendist.post_service.domain.review.dto.response;

public record ReviewMonthlyCountResponse(
	Integer year,
	Integer month,
	Long count
) {
}

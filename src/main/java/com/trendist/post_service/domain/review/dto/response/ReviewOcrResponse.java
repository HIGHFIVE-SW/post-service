package com.trendist.post_service.domain.review.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewOcrResponse {
	@JsonProperty("award_ocr_result")
	private String awardOcrResult;

	@JsonProperty("ocr_result")
	private String ocrResult;

	@JsonProperty("review_id")
	private String reviewId;
}

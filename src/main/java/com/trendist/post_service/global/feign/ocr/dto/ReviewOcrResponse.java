package com.trendist.post_service.global.feign.ocr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ReviewOcrResponse(

	@JsonProperty("awardOcrResult")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Boolean awardOcrResult,

	@JsonProperty("ocrResult")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Boolean ocrResult
	) {
}

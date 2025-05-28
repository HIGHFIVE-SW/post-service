package com.trendist.post_service.global.feign.ocr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public record OcrResponse(

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Boolean awardOcrResult,

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	Boolean ocrResult
	) {
}

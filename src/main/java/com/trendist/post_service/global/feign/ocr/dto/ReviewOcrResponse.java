package com.trendist.post_service.global.feign.ocr.dto;

public record ReviewOcrResponse(
	Boolean awardOcrResult,
	Boolean ocrResult
) {
}
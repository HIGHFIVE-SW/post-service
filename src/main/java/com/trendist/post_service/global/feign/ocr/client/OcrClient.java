package com.trendist.post_service.global.feign.ocr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.trendist.post_service.global.feign.ocr.dto.ReviewOcrRequest;
import com.trendist.post_service.global.feign.ocr.dto.ReviewOcrResponse;

@FeignClient(
	name = "ocr-service",
	url = "${review.ocr.base-url}",
	configuration = com.trendist.post_service.global.config.feign.FeignAuthConfig.class)
public interface OcrClient {
	/**
	 * POST /ocr
	 *   Body: ReviewOcrRequest
	 *   Response: ReviewOcrResponse
	 */
	@PostMapping("/ocr")
	ReviewOcrResponse verifyImgOcr(ReviewOcrRequest reviewOcrRequest);
}

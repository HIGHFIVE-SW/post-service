package com.trendist.post_service.global.feign.ocr.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.trendist.post_service.global.feign.ocr.dto.ReviewOcrRequest;
import com.trendist.post_service.global.feign.ocr.dto.ReviewOcrResponse;

@FeignClient(
	name = "ocr-service",
	url = "${review.ocr.base-url}"
)
public interface OcrClient {
	/**
	 * POST
	 *   Body: ReviewOcrRequest
	 *   Response: ReviewOcrResponse
	 */
	@PostMapping(
		path     = "/",
		consumes = MediaType.APPLICATION_JSON_VALUE
	)
	ReviewOcrResponse verifyImgOcr(@RequestBody ReviewOcrRequest reviewOcrRequest);
}

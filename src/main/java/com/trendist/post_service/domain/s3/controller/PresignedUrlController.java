package com.trendist.post_service.domain.s3.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.global.response.ApiResponse;
import com.trendist.post_service.domain.s3.dto.request.PresignedUrlRequest;
import com.trendist.post_service.domain.s3.dto.response.PresignedUrlResponse;
import com.trendist.post_service.domain.s3.service.PresignedUrlService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PresignedUrlController {
	private final PresignedUrlService presignedUrlService;

	@Operation(
		summary = "PresignedUrl 발급",
		description = "s3 버킷에 이미지를 업로드하기 위한 PresignedUrl을 발급합니다."
	)
	@PostMapping("/presignedurls")
	public ApiResponse<PresignedUrlResponse> getPresignedUrl(@RequestBody PresignedUrlRequest presignedUrlRequest) {
		return ApiResponse.onSuccess(presignedUrlService.getPreSignedUrl(presignedUrlRequest.imageNames()));
	}
}

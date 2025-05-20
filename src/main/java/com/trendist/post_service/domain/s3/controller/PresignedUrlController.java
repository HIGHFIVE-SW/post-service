package com.trendist.post_service.domain.s3.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.domain.s3.dto.request.PresignedUrlRequest;
import com.trendist.post_service.domain.s3.dto.response.PresignedUrlResponse;
import com.trendist.post_service.global.response.ApiResponse;
import com.trendist.post_service.domain.s3.dto.request.PresignedUrlsRequest;
import com.trendist.post_service.domain.s3.dto.response.PresignedUrlsResponse;
import com.trendist.post_service.domain.s3.service.PresignedUrlService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/presignedurls")
public class PresignedUrlController {
	private final PresignedUrlService presignedUrlService;

	@Operation(
		summary = "PresignedUrl 여러개 발급",
		description = "s3 버킷에 이미지를 업로드하기 위한 PresignedUrl을 여러개 발급합니다."
	)
	@PostMapping("/images")
	public ApiResponse<PresignedUrlsResponse> getPresignedUrls(@RequestBody PresignedUrlsRequest presignedUrlsRequest) {
		return ApiResponse.onSuccess(presignedUrlService.getPreSignedUrls(presignedUrlsRequest.imageNames()));
	}

	@Operation(
		summary = "PresignedUrl 발급",
		description = "s3 버킷에 이미지를 업로드하기 위한 PresignedUrl을 발급합니다."
	)
	@PostMapping("/image")
	public ApiResponse<PresignedUrlResponse> getPresignedUrl(@RequestBody PresignedUrlRequest presignedUrlRequest) {
		return ApiResponse.onSuccess(presignedUrlService.getPreSignedUrl(presignedUrlRequest.imageName()));
	}
}

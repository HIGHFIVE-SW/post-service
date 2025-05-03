package com.trendist.post_service.s3.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.global.response.ApiResponse;
import com.trendist.post_service.s3.dto.request.PresignedUrlRequest;
import com.trendist.post_service.s3.dto.response.PresignedUrlResponse;
import com.trendist.post_service.s3.service.PresignedUrlService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PresignedUrlController {
	private final PresignedUrlService presignedUrlService;

	@PostMapping("/presignedurl")
	public ApiResponse<PresignedUrlResponse> getPresignedUrl(@RequestBody PresignedUrlRequest presignedUrlRequest) {
		return ApiResponse.onSuccess(presignedUrlService.getPreSignedUrl(presignedUrlRequest.imageName()));
	}
}

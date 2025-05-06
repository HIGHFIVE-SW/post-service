package com.trendist.post_service.domain.s3.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record PresignedUrlResponse(
	List<String> PresignedUrls
) {
	public static PresignedUrlResponse from(List<String> urls) {
		return PresignedUrlResponse.builder()
			.PresignedUrls(urls)
			.build();
	}
}

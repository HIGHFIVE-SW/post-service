package com.trendist.post_service.domain.s3.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record PresignedUrlsResponse(
	List<String> PresignedUrls
) {
	public static PresignedUrlsResponse from(List<String> urls) {
		return PresignedUrlsResponse.builder()
			.PresignedUrls(urls)
			.build();
	}
}

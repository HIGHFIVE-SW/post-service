package com.trendist.post_service.s3.dto.response;

import lombok.Builder;

@Builder
public record PresignedUrlResponse(
	String PresignedUrl
) {
	public static PresignedUrlResponse from(String url) {
		return PresignedUrlResponse.builder()
			.PresignedUrl(url)
			.build();
	}
}

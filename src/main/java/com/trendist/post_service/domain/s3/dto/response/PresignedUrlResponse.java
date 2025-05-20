package com.trendist.post_service.domain.s3.dto.response;

import lombok.Builder;

@Builder
public record PresignedUrlResponse(
	String presignedUrl
) {
	public static PresignedUrlResponse from(String url) {
		return PresignedUrlResponse.builder()
			.presignedUrl(url)
			.build();
	}
}

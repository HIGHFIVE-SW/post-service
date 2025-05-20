package com.trendist.post_service.domain.s3.dto.request;

public record PresignedUrlRequest(
	String imageName
) {
}

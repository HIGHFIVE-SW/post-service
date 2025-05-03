package com.trendist.post_service.s3.dto.request;

public record PresignedUrlRequest(
	String imageName
) {
}

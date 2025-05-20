package com.trendist.post_service.domain.s3.dto.request;

import java.util.List;

public record PresignedUrlsRequest(
	List<String> imageNames
) {
}

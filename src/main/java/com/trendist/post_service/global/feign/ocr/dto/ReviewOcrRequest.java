package com.trendist.post_service.global.feign.ocr.dto;

import java.util.List;

public record ReviewOcrRequest (
	String awardImageUrl,
	List<String> imageUrls
) {
}

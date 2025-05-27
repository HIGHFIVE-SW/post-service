package com.trendist.post_service.global.feign.ocr.dto;

import java.util.List;

public record ReviewOcrRequest (
	List<String> imageUrls,
	String awardImageUrl,
	String title
) {
}

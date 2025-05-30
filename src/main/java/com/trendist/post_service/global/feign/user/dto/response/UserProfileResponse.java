package com.trendist.post_service.global.feign.user.dto.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserProfileResponse(
	UUID id,
	String username,
	String email,
	String nickname,
	String keyword,
	String profileUrl,
	int exp,
	String tierName,
	String tierImageUrl
) {
}

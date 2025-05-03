package com.trendist.post_service.global.feign.user.dto;

import java.util.Set;
import java.util.UUID;

import com.trendist.post_service.post.domain.Keyword;

import lombok.Builder;

@Builder
public record UserProfileResponse(
	UUID id,
	String username,
	String email,
	String nickname,
	Set<String> keywords,
	int exp,
	String tierName,
	String tierImageUrl
) {
}

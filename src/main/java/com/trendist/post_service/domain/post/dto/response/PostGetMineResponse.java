package com.trendist.post_service.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.Post;

import lombok.Builder;

@Builder
public record PostGetMineResponse(
	UUID id,
	String title,
	LocalDateTime updatedAt
) {
	public static PostGetMineResponse from(Post post) {
		return PostGetMineResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.updatedAt(post.getUpdatedAt())
			.build();
	}
}

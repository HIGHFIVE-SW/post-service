package com.trendist.post_service.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.Post;

import lombok.Builder;

@Builder
public record PostGetAllResponse(
	UUID id,
	String title,
	String nickname,
	Integer likeCount,
	LocalDateTime createdAt
) {
	public static PostGetAllResponse from(Post post) {
		return PostGetAllResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.nickname(post.getNickname())
			.likeCount(post.getLikeCount())
			.createdAt(post.getCreatedAt())
			.build();
	}
}

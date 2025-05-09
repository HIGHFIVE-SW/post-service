package com.trendist.post_service.domain.post.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.post.domain.PostLike;

import lombok.Builder;

@Builder
public record PostLikeResponse(
	UUID likeId,
	UUID postId,
	UUID userId,
	Boolean like
) {
	public static PostLikeResponse of(PostLike postLike, Boolean like) {
		return PostLikeResponse.builder()
			.likeId(postLike.getId())
			.postId(postLike.getPostId())
			.userId(postLike.getUserId())
			.like(like)
			.build();
	}
}

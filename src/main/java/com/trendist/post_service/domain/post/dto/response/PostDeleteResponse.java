package com.trendist.post_service.domain.post.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.post.domain.Post;

import lombok.Builder;

@Builder
public record PostDeleteResponse(
	UUID id,
	String title,
	Boolean deleted
) {
	public static PostDeleteResponse from(Post post) {
		return PostDeleteResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.deleted(post.getDeleted())
			.build();
	}
}

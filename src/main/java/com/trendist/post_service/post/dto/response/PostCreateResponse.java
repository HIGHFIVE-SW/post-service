package com.trendist.post_service.post.dto.response;

import java.util.Set;
import java.util.UUID;

import com.trendist.post_service.post.domain.Keyword;
import com.trendist.post_service.post.domain.Post;

import lombok.Builder;

@Builder
public record PostCreateResponse(
	UUID id,
	String title,
	String content,
	Set<Keyword> keywords,
	Boolean deleted
) {
	public static PostCreateResponse from(Post post) {
		return PostCreateResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.keywords(post.getKeywords())
			.deleted(post.getDeleted())
			.build();
	}
}

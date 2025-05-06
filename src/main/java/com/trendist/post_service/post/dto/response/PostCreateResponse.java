package com.trendist.post_service.post.dto.response;

import java.util.List;
import java.util.UUID;

import com.trendist.post_service.post.domain.Post;

import lombok.Builder;

@Builder
public record PostCreateResponse(
	UUID id,
	String title,
	UUID userId,
	String nickname,
	String content,
	List<String> imageUrls,
	Boolean deleted
) {
	public static PostCreateResponse from(Post post) {
		return PostCreateResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.userId(post.getUserId())
			.nickname(post.getNickname())
			.content(post.getContent())
			.imageUrls(post.getImageUrls())
			.deleted(post.getDeleted())
			.build();
	}
}

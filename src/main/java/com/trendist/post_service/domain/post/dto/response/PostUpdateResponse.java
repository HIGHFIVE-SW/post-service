package com.trendist.post_service.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.Post;

import lombok.Builder;

@Builder
public record PostUpdateResponse(
	UUID id,
	String title,
	String content,
	List<String> imageUrls,
	LocalDateTime updatedAt
) {
	public static PostUpdateResponse from(Post post) {
		return PostUpdateResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.imageUrls(post.getImageUrls())
			.updatedAt(post.getUpdatedAt())
			.build();
	}
}

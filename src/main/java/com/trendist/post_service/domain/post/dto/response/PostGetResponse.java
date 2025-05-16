package com.trendist.post_service.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.Post;

import lombok.Builder;

@Builder
public record PostGetResponse(
	UUID id,
	String title,
	String content,
	List<String> imageUrls,
	Integer likeCount,
	UUID userId,
	String nickname,
	String profileUrl,
	LocalDateTime createdAt
) {
	public static PostGetResponse of(Post post, String profileUrl) {
		return PostGetResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.imageUrls(post.getImageUrls())
			.likeCount(post.getLikeCount())
			.userId(post.getUserId())
			.nickname(post.getNickname())
			.profileUrl(profileUrl)
			.createdAt(post.getCreatedAt())
			.build();
	}
}

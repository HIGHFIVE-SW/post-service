package com.trendist.post_service.domain.post.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.Post;

import lombok.Builder;

@Builder
public record PostGetResponse(
	UUID id,
	String title,
	String content,
	UUID userId,
	String nickname,
	String profileUrl,
	LocalDateTime createdAt
	//좋아요, 댓글 추가되어야함
) {
	public static PostGetResponse of(Post post, String profileUrl) {
		return PostGetResponse.builder()
			.id(post.getId())
			.title(post.getTitle())
			.content(post.getContent())
			.userId(post.getUserId())
			.nickname(post.getNickname())
			.profileUrl(profileUrl)
			.createdAt(post.getCreatedAt())
			.build();
	}
}

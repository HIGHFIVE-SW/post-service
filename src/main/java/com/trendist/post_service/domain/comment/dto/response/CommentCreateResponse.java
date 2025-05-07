package com.trendist.post_service.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.comment.domain.Comment;

import lombok.Builder;

@Builder
public record CommentCreateResponse(
	UUID commentId,
	UUID postId,
	UUID userId,
	String nickname,
	String profileUrl,
	String content,
	LocalDateTime createdAt
) {
	public static CommentCreateResponse of(Comment comment, String nickname, String profileUrl) {
		return CommentCreateResponse.builder()
			.commentId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUserId())
			.nickname(nickname)
			.profileUrl(profileUrl)
			.content(comment.getContent())
			.createdAt(comment.getCreatedAt())
			.build();
	}
}

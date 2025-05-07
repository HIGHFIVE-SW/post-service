package com.trendist.post_service.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.comment.domain.Comment;

import lombok.Builder;

@Builder
public record CommentUpdateResponse(
	UUID commentId,
	UUID postId,
	UUID userId,
	String content,
	LocalDateTime updatedAt
) {
	public static CommentUpdateResponse from(Comment comment) {
		return CommentUpdateResponse.builder()
			.commentId(comment.getId())
			.postId(comment.getPost().getId())
			.userId(comment.getUserId())
			.content(comment.getContent())
			.updatedAt(comment.getUpdatedAt())
			.build();
	}
}

package com.trendist.post_service.domain.comment.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.comment.domain.Comment;

import lombok.Builder;

@Builder
public record CommentDeleteResponse(
	UUID postId,
	UUID commentId,
	UUID userId,
	Boolean deleted
) {
	public static CommentDeleteResponse from(Comment comment) {
		return CommentDeleteResponse.builder()
			.postId(comment.getPost().getId())
			.commentId(comment.getId())
			.userId(comment.getUserId())
			.deleted(comment.getDeleted())
			.build();
	}
}

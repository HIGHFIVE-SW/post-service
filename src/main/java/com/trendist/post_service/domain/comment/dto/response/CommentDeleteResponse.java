package com.trendist.post_service.domain.comment.dto.response;

import java.util.UUID;

import com.trendist.post_service.domain.comment.domain.Comment;

import lombok.Builder;

@Builder
public record CommentDeleteResponse(
	UUID commentId,
	UUID postId,
	UUID reviewId,
	UUID userId,
	Boolean deleted
) {
	public static CommentDeleteResponse from(Comment comment) {
		UUID postId = comment.getPost() != null ? comment.getPost().getId() : null;
		UUID reviewId = comment.getReview() != null ? comment.getReview().getId() : null;

		return CommentDeleteResponse.builder()
			.commentId(comment.getId())
			.postId(postId)
			.reviewId(reviewId)
			.userId(comment.getUserId())
			.deleted(comment.getDeleted())
			.build();
	}
}

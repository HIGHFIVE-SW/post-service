package com.trendist.post_service.domain.comment.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.trendist.post_service.domain.comment.domain.Comment;

import lombok.Builder;

@Builder
public record CommentGetAllResponse(
	UUID commentId,
	UUID postId,
	UUID userId,
	String nickname,
	String profileUrl,
	String content,
	LocalDateTime createdAt
) {
	public static CommentGetAllResponse of(Comment comment, String nickname, String profileUrl) {
		return CommentGetAllResponse.builder()
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

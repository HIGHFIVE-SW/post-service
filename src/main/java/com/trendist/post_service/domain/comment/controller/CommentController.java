package com.trendist.post_service.domain.comment.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.domain.comment.dto.request.CommentCreateOrUpdateRequest;
import com.trendist.post_service.domain.comment.dto.response.CommentCreateResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentGetAllResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentUpdateResponse;
import com.trendist.post_service.domain.comment.service.CommentService;
import com.trendist.post_service.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts/comments")
public class CommentController {
	private final CommentService commentService;

	@Operation(
		summary = "게시물 댓글 작성",
		description = "특정 게시물에 댓글을 작성합니다."
	)
	@PostMapping("/{postId}")
	public ApiResponse<CommentCreateResponse> createComment(
		@PathVariable(name = "postId") UUID postId,
		@RequestBody CommentCreateOrUpdateRequest commentCreateOrUpdateRequest
	) {
		return ApiResponse.onSuccess(commentService.createComment(postId, commentCreateOrUpdateRequest));
	}

	@Operation(
		summary = "댓글 수정",
		description = "특정 댓글을 수정합니다."
	)
	@PatchMapping("/{commentId}")
	public ApiResponse<CommentUpdateResponse> updateComment(
		@PathVariable(name = "commentId") UUID commentId,
		@RequestBody CommentCreateOrUpdateRequest commentCreateOrUpdateRequest
	) {
		return ApiResponse.onSuccess(commentService.updateComment(commentId, commentCreateOrUpdateRequest));
	}

	@Operation(
		summary = "댓글 확인",
		description = "특정 게시물에 작성된 모든 댓글을 확인합니다."
	)
	@GetMapping("/{postId}")
	public ApiResponse<List<CommentGetAllResponse>> getPostComments(
		@PathVariable(name = "postId") UUID postId) {
		return ApiResponse.onSuccess(commentService.getPostComments(postId));
	}
}

package com.trendist.post_service.domain.post.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.domain.post.dto.request.PostUpdateRequest;
import com.trendist.post_service.domain.post.dto.response.PostDeleteResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetAllResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetMineResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetResponse;
import com.trendist.post_service.domain.post.dto.response.PostLikeResponse;
import com.trendist.post_service.domain.post.dto.response.PostUpdateResponse;
import com.trendist.post_service.global.response.ApiResponse;
import com.trendist.post_service.domain.post.dto.request.PostCreateRequest;
import com.trendist.post_service.domain.post.dto.response.PostCreateResponse;
import com.trendist.post_service.domain.post.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	@Operation(
		summary = "자유 게시판 게시물 생성",
		description = "사용자가 자유 게시판에 게시물을 생성합니다."
	)
	@PostMapping
	public ApiResponse<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
		return ApiResponse.onSuccess(postService.createPost(postCreateRequest));
	}

	@Operation(
		summary = "자유 게시판 게시물 수정",
		description = "사용자가 자유 게시판에 게시물을 수정합니다."
	)
	@PatchMapping("/update/{postId}")
	public ApiResponse<PostUpdateResponse> updatePost(
		@PathVariable(name = "postId") UUID postId,
		@RequestBody PostUpdateRequest postUpdateRequest) {
		return ApiResponse.onSuccess(postService.updatePost(postId, postUpdateRequest));
	}

	@Operation(
		summary = "자유 게시판 게시물 삭제",
		description = "사용자가 자유 게시판에 게시물을 삭제합니다."
	)
	@PatchMapping("/delete/{postId}")
	public ApiResponse<PostDeleteResponse> deletePost(@PathVariable(name = "postId") UUID postId) {
		return ApiResponse.onSuccess(postService.deletePost(postId));
	}

	@Operation(
		summary = "자유 게시판 전체 게시물 조회",
		description = "자유 게시판에 전체 게시물을 조회합니다."
	)
	@GetMapping
	public ApiResponse<Page<PostGetAllResponse>> getAllPosts(@RequestParam(defaultValue = "0") int page) {
		return ApiResponse.onSuccess(postService.getAllPosts(page));
	}

	@Operation(
		summary = "자유 게시판 특정 게시물 조회",
		description = "사용자가 자유 게시판에 있는 특정 게시물을 상세 조회합니다."
	)
	@GetMapping("/{postId}")
	public ApiResponse<PostGetResponse> getPost(@PathVariable(name = "postId") UUID postId) {
		return ApiResponse.onSuccess(postService.getPost(postId));
	}

	@Operation(
		summary = "내가 쓴 자유 게시판 게시물 조회",
		description = "현재 로그인한 사용자가 자유 게시판에 자신이 생성한 게시물들을 조회합니다."
	)
	@GetMapping("/mine")
	public ApiResponse<Page<PostGetMineResponse>> getMyPosts(@RequestParam(defaultValue = "0") int page) {
		return ApiResponse.onSuccess(postService.getMyPosts(page));
	}

	@Operation(
		summary = "자유 게시판 게시물 좋아요",
		description = "사용자가 자유 게시판에 있는 특정 게시물에 좋아요를 누르거나 취소합니다."
	)
	@PostMapping("/{postId}/like")
	public ApiResponse<PostLikeResponse> likePost(@PathVariable(name = "postId") UUID postId) {
		return ApiResponse.onSuccess(postService.likePost(postId));
	}
}

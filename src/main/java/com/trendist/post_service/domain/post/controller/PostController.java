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
import com.trendist.post_service.domain.post.dto.response.PostGetResponse;
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

	@PatchMapping("/update/{postId}")
	public ApiResponse<PostUpdateResponse> updatePost(
		@PathVariable(name = "postId") UUID postId,
		@RequestBody PostUpdateRequest postUpdateRequest) {
		return ApiResponse.onSuccess(postService.updatePost(postId, postUpdateRequest));
	}

	@PatchMapping("/delete/{postId}")
	public ApiResponse<PostDeleteResponse> deletePost(@PathVariable(name = "postId") UUID postId) {
		return ApiResponse.onSuccess(postService.deletePost(postId));
	}

	@GetMapping
	public ApiResponse<Page<PostGetAllResponse>> getAllPosts(@RequestParam(defaultValue = "0") int page) {
		return ApiResponse.onSuccess(postService.getAllPosts(page));
	}

	@GetMapping("/{postId}")
	public ApiResponse<PostGetResponse> getPost(@PathVariable(name = "postId") UUID postId) {
		return ApiResponse.onSuccess(postService.getPost(postId));
	}
}

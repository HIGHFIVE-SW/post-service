package com.trendist.post_service.domain.post.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

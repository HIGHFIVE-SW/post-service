package com.trendist.post_service.post.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.global.response.ApiResponse;
import com.trendist.post_service.post.dto.request.PostCreateRequest;
import com.trendist.post_service.post.dto.response.PostCreateResponse;
import com.trendist.post_service.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

	@PostMapping
	public ApiResponse<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
		return ApiResponse.onSuccess(postService.createPost(postCreateRequest));
	}
}

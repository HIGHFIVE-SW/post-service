package com.trendist.post_service.domain.post.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendist.post_service.domain.post.dto.response.PostGetUserResponse;
import com.trendist.post_service.domain.post.service.PostProfileService;
import com.trendist.post_service.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile/posts")
public class PostProfileController {
	private final PostProfileService postProfileService;

	@Operation(
		summary = "내가 쓴 자유 게시판 게시물 조회",
		description = "현재 로그인한 사용자가 자유 게시판에 자신이 생성한 게시물들을 조회합니다."
	)
	@GetMapping("/mine")
	public ApiResponse<Page<PostGetUserResponse>> getMyPosts(@RequestParam(defaultValue = "0") int page) {
		return ApiResponse.onSuccess(postProfileService.getMyPosts(page));
	}

	@Operation(
		summary = "특정 사용자가 쓴 자유 게시판 게시물 조회",
		description = "특정 사용자가 자유 게시판에 본인이 생성한 게시물들을 조회합니다."
	)
	@GetMapping("/{userId}")
	public ApiResponse<Page<PostGetUserResponse>> getUserPosts(
		@RequestParam(defaultValue = "0") int page,
		@PathVariable(name = "userId") UUID userId) {
		return ApiResponse.onSuccess(postProfileService.getUserPosts(page, userId));
	}
}

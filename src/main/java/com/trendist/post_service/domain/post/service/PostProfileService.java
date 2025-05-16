package com.trendist.post_service.domain.post.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.post.dto.response.PostGetUserResponse;
import com.trendist.post_service.domain.post.repository.PostRepository;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostProfileService {
	private final PostRepository postRepository;
	private final UserServiceClient userServiceClient;

	public Page<PostGetUserResponse> getMyPosts(int page) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

		return postRepository.findByUserIdAndDeletedFalse(userId, pageable)
			.map(PostGetUserResponse::from);
	}

	public Page<PostGetUserResponse> getUserPosts(int page, UUID userId) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("updatedAt").descending());

		return postRepository.findByUserIdAndDeletedFalse(userId, pageable)
			.map(PostGetUserResponse::from);
	}
}

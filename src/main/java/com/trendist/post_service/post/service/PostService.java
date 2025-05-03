package com.trendist.post_service.post.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.post.domain.Post;
import com.trendist.post_service.post.dto.request.PostCreateRequest;
import com.trendist.post_service.post.dto.response.PostCreateResponse;
import com.trendist.post_service.post.repository.PostRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final UserServiceClient userServiceClient;

	@Transactional
	public PostCreateResponse createPost(PostCreateRequest postCreateRequest) {
		UUID userId = userServiceClient.getUserProfile("").getResult().id();

		Post post = Post.builder()
			.title(postCreateRequest.title())
			.content(postCreateRequest.content())
			.keywords(postCreateRequest.keywords())
			.userId(userId)
			.build();

		postRepository.save(post);

		return PostCreateResponse.from(post);
	}
}

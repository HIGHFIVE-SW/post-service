package com.trendist.post_service.domain.post.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.post.dto.response.PostGetAllResponse;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.domain.post.domain.Post;
import com.trendist.post_service.domain.post.dto.request.PostCreateRequest;
import com.trendist.post_service.domain.post.dto.response.PostCreateResponse;
import com.trendist.post_service.domain.post.repository.PostRepository;

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
		String nickname = userServiceClient.getUserProfile("").getResult().nickname();

		Post post = Post.builder()
			.title(postCreateRequest.title())
			.content(postCreateRequest.content())
			.imageUrls(postCreateRequest.imageUrls())
			.userId(userId)
			.nickname(nickname)
			.build();

		postRepository.save(post);

		return PostCreateResponse.from(post);
	}

	public Page<PostGetAllResponse> getAllPosts(int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
		return postRepository.findAllByDeletedFalse(pageable)
			.map(PostGetAllResponse::from);
	}
}

package com.trendist.post_service.domain.post.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.post.dto.response.PostGetAllResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetResponse;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.domain.post.domain.Post;
import com.trendist.post_service.domain.post.dto.request.PostCreateRequest;
import com.trendist.post_service.domain.post.dto.response.PostCreateResponse;
import com.trendist.post_service.domain.post.repository.PostRepository;
import com.trendist.post_service.global.response.status.ErrorStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final UserServiceClient userServiceClient;

	@Transactional
	public PostCreateResponse createPost(PostCreateRequest postCreateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		String nickname = userServiceClient.getMyProfile("").getResult().nickname();

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

	public PostGetResponse getPost(UUID postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));
		String profileUrl = userServiceClient.getUserProfile(post.getUserId()).getResult().profileUrl();

		return PostGetResponse.of(post, profileUrl);
	}
}

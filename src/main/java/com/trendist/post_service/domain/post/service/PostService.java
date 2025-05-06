package com.trendist.post_service.domain.post.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.post.dto.request.PostUpdateRequest;
import com.trendist.post_service.domain.post.dto.response.PostDeleteResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetAllResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetResponse;
import com.trendist.post_service.domain.post.dto.response.PostUpdateResponse;
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

	@Transactional
	public PostUpdateResponse updatePost(UUID postId, PostUpdateRequest postUpdateRequest) {
		Post post = postRepository.findByIdAndDeletedFalse(postId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));

		UUID nowUserId = userServiceClient.getMyProfile("").getResult().id();
		if (!nowUserId.equals(post.getUserId())) {
			throw new ApiException(ErrorStatus._POST_UPDATE_FORBIDDEN);
		}

		post.setTitle(postUpdateRequest.title());
		post.setContent(postUpdateRequest.content());
		post.setImageUrls(postUpdateRequest.imageUrls());

		postRepository.save(post);

		return PostUpdateResponse.from(post);
	}

	@Transactional
	public PostDeleteResponse deletePost(UUID postId){
		Post post = postRepository.findByIdAndDeletedFalse(postId)
			.orElseThrow(()-> new ApiException(ErrorStatus._POST_NOT_FOUND));

		UUID nowUserId = userServiceClient.getMyProfile("").getResult().id();
		if (!nowUserId.equals(post.getUserId())) {
			throw new ApiException(ErrorStatus._POST_DELETE_FORBIDDEN);
		}

		post.setDeleted(true);

		postRepository.save(post);

		return PostDeleteResponse.from(post);
	}

	public Page<PostGetAllResponse> getAllPosts(int page) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
		return postRepository.findAllByDeletedFalse(pageable)
			.map(PostGetAllResponse::from);
	}

	public PostGetResponse getPost(UUID postId) {
		Post post = postRepository.findByIdAndDeletedFalse(postId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));
		String profileUrl = userServiceClient.getUserProfile(post.getUserId()).getResult().profileUrl();

		return PostGetResponse.of(post, profileUrl);
	}
}

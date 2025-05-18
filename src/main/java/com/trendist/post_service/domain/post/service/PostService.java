package com.trendist.post_service.domain.post.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.post.domain.PostDocument;
import com.trendist.post_service.domain.post.domain.PostLike;
import com.trendist.post_service.domain.post.dto.request.PostUpdateRequest;
import com.trendist.post_service.domain.post.dto.response.PostDeleteResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetAllResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetResponse;
import com.trendist.post_service.domain.post.dto.response.PostGetSearchResponse;
import com.trendist.post_service.domain.post.dto.response.PostLikeResponse;
import com.trendist.post_service.domain.post.dto.response.PostUpdateResponse;
import com.trendist.post_service.domain.post.repository.PostLikeRepository;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.domain.post.domain.Post;
import com.trendist.post_service.domain.post.dto.request.PostCreateRequest;
import com.trendist.post_service.domain.post.dto.response.PostCreateResponse;
import com.trendist.post_service.domain.post.repository.PostRepository;
import com.trendist.post_service.global.response.status.ErrorStatus;

import co.elastic.clients.elasticsearch._types.SortOrder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final PostLikeRepository postLikeRepository;
	private final UserServiceClient userServiceClient;
	private final ElasticsearchOperations esOps;

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
	public PostDeleteResponse deletePost(UUID postId) {
		Post post = postRepository.findByIdAndDeletedFalse(postId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));

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

	public Page<PostGetAllResponse> getAllPostsByLikeCount(int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return postRepository.findAllByDeletedFalseOrderByLikeCountDesc(pageable)
			.map(PostGetAllResponse::from);
	}

	public PostGetResponse getPost(UUID postId) {
		Post post = postRepository.findByIdAndDeletedFalse(postId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));
		String profileUrl = userServiceClient.getUserProfile(post.getUserId()).getResult().profileUrl();

		return PostGetResponse.of(post, profileUrl);
	}

	@Transactional
	public PostLikeResponse likePost(UUID postId) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		PostLike postLike;
		boolean like;

		if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
			postLike = PostLike.builder()
				.postId(postId)
				.userId(userId)
				.build();
			postLikeRepository.save(postLike);
			postRepository.incrementLikeCount(postId);
			like = true;
		} else {
			postLike = postLikeRepository.findByPostIdAndUserId(postId, userId)
				.orElseThrow(() -> new ApiException(ErrorStatus._POST_LIKE_NOT_FOUND));
			postLikeRepository.deleteByPostIdAndUserId(postId, userId);
			postRepository.decrementLikeCount(postId);
			like = false;
		}
		return PostLikeResponse.of(postLike, like);
	}

	public Page<PostGetSearchResponse> searchPosts(String keyword, int page) {
		Pageable pageable = PageRequest.of(page, 10);

		NativeQuery query = NativeQuery.builder()
			.withQuery(q -> q
				.wildcard(w -> w
					.field("post_title.keyword")
					.value("*" + keyword + "*")
				)
			)
			.withSort(s -> s.field(f -> f
					.field("created_at.keyword")
					.order(SortOrder.Desc)
				)
			)
			.withPageable(pageable)
			.build();

		// 3) 검색 실행
		SearchHits<PostDocument> hits = esOps.search(query, PostDocument.class);

		List<PostGetSearchResponse> content = hits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.map(PostGetSearchResponse::from)
			.toList();

		return new PageImpl<>(content, pageable, hits.getTotalHits());
	}
}

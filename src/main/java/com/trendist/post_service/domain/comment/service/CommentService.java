package com.trendist.post_service.domain.comment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.comment.domain.Comment;
import com.trendist.post_service.domain.comment.dto.request.CommentCreateOrUpdateRequest;
import com.trendist.post_service.domain.comment.dto.response.CommentCreateResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentGetAllResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentUpdateResponse;
import com.trendist.post_service.domain.comment.repository.CommentRepository;
import com.trendist.post_service.domain.post.domain.Post;
import com.trendist.post_service.domain.post.repository.PostRepository;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.global.feign.user.dto.UserProfileResponse;
import com.trendist.post_service.global.response.status.ErrorStatus;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final UserServiceClient userServiceClient;
	private final PostRepository postRepository;

	@Transactional
	public CommentCreateResponse createComment(UUID postId, CommentCreateOrUpdateRequest commentCreateOrUpdateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		Post post = postRepository.findByIdAndDeletedFalse(postId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));

		Comment comment = Comment.builder()
			.post(post)
			.userId(userId)
			.content(commentCreateOrUpdateRequest.content())
			.build();

		commentRepository.save(comment);

		return CommentCreateResponse.from(comment);
	}

	@Transactional
	public CommentUpdateResponse updateComment(UUID commentId,
		CommentCreateOrUpdateRequest commentCreateOrUpdateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		Comment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new ApiException(ErrorStatus._POST_NOT_FOUND));
		//충돌 안나게 ErrorStatus 추가는 리뷰 작업 끝나고 진행

		if (!userId.equals(comment.getUserId())) {
			throw new ApiException(ErrorStatus._POST_UPDATE_FORBIDDEN);
			//충돌안나게 ErrorStatus 추가는 리뷰 작업 끝나고 진행
		}

		comment.setContent(commentCreateOrUpdateRequest.content());
		commentRepository.save(comment);

		return CommentUpdateResponse.from(comment);
	}

	public List<CommentGetAllResponse> getPostComments(UUID postId) {
		List<Comment> comments = commentRepository.findAllByPostId(postId);

		return comments.stream()
			.map(comment -> {
				UserProfileResponse user = userServiceClient.getUserProfile(comment.getUserId()).getResult();
				return CommentGetAllResponse.of(comment, user.nickname(), user.profileUrl());
			})
			.toList();
	}
}

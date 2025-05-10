package com.trendist.post_service.domain.comment.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.trendist.post_service.domain.comment.domain.Comment;
import com.trendist.post_service.domain.comment.dto.request.CommentCreateOrUpdateRequest;
import com.trendist.post_service.domain.comment.dto.response.CommentCreateResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentDeleteResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentGetAllResponse;
import com.trendist.post_service.domain.comment.dto.response.CommentUpdateResponse;
import com.trendist.post_service.domain.comment.repository.CommentRepository;
import com.trendist.post_service.domain.post.domain.Post;
import com.trendist.post_service.domain.post.repository.PostRepository;
import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.repository.ReviewRepository;
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
	private final ReviewRepository reviewRepository;

	@Transactional
	public CommentCreateResponse createComment(UUID id, CommentCreateOrUpdateRequest commentCreateOrUpdateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		Comment comment;

		Optional<Post> post = postRepository.findByIdAndDeletedFalse(id);
		if (post.isPresent()) {
			comment = Comment.builder()
				.post(post.get())
				.userId(userId)
				.content(commentCreateOrUpdateRequest.content())
				.build();
		} else {
			Review review = reviewRepository.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));

			comment = Comment.builder()
				.review(review)
				.userId(userId)
				.content(commentCreateOrUpdateRequest.content())
				.build();
		}

		commentRepository.save(comment);
		return CommentCreateResponse.from(comment);
	}

	@Transactional
	public CommentUpdateResponse updateComment(UUID commentId,
		CommentCreateOrUpdateRequest commentCreateOrUpdateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
			.orElseThrow(() -> new ApiException(ErrorStatus._COMMENT_NOT_FOUND));

		if (!userId.equals(comment.getUserId())) {
			throw new ApiException(ErrorStatus._COMMENT_UPDATE_FORBIDDEN);
		}

		comment.setContent(commentCreateOrUpdateRequest.content());
		commentRepository.save(comment);

		return CommentUpdateResponse.from(comment);
	}

	@Transactional
	public CommentDeleteResponse deleteComment(UUID commentId) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		Comment comment = commentRepository.findByIdAndDeletedFalse(commentId)
			.orElseThrow(() -> new ApiException(ErrorStatus._COMMENT_NOT_FOUND));

		if (!userId.equals(comment.getUserId())) {
			throw new ApiException(ErrorStatus._COMMENT_DELETE_FORBIDDEN);
		}

		comment.setDeleted(true);
		commentRepository.save(comment);

		return CommentDeleteResponse.from(comment);
	}

	public List<CommentGetAllResponse> getPostComments(UUID id) {
		List<Comment> comments = postRepository.findByIdAndDeletedFalse(id)
			.map(post -> commentRepository.findAllByPostIdAndDeletedFalse(id))
			.orElseGet(() -> {
				reviewRepository.findByIdAndDeletedFalse(id)
					.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));
				return commentRepository.findAllByReviewIdAndDeletedFalse(id);
			});

		return comments.stream()
			.map(comment -> {
				UserProfileResponse user = userServiceClient.getUserProfile(comment.getUserId()).getResult();
				return CommentGetAllResponse.of(comment, user.nickname(), user.profileUrl());
			})
			.toList();
	}
}

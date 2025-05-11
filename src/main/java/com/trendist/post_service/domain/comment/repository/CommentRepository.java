package com.trendist.post_service.domain.comment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
	List<Comment> findAllByPostIdAndDeletedFalse(UUID postId);

	List<Comment> findAllByReviewIdAndDeletedFalse(UUID reviewId);

	Optional<Comment> findByIdAndDeletedFalse(UUID commentId);
}

package com.trendist.post_service.domain.review.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.domain.review.domain.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, UUID> {
	Optional<ReviewLike> findByReviewIdAndUserId(UUID reviewId, UUID userId);

	boolean existsByReviewIdAndUserId(UUID reviewId, UUID userId);

	void deleteByReviewIdAndUserId(UUID postId, UUID userId);
}

package com.trendist.post_service.domain.review.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trendist.post_service.domain.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
	Page<Review> findAllByDeletedFalse(Pageable pageable);

	Optional<Review> findByIdAndDeletedFalse(UUID id);

	Page<Review> findByUserIdAndDeletedFalse(UUID id, Pageable pageable);

	Page<Review> findAllByDeletedFalseOrderByLikeCountDesc(Pageable pageable);

	@Modifying
	@Query("UPDATE reviews r SET r.likeCount = r.likeCount + 1 WHERE r.id = :reviewId")
	void incrementLikeCount(@Param("reviewId") UUID reviewId);

	@Modifying
	@Query("UPDATE reviews r SET r.likeCount = r.likeCount - 1 WHERE r.id = :reviewId AND r.likeCount > 0")
	void decrementLikeCount(@Param("reviewId") UUID reviewId);
}

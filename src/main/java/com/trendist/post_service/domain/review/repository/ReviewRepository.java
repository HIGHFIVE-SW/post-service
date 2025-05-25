package com.trendist.post_service.domain.review.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
	Page<Review> findAllByDeletedFalse(Pageable pageable);

	Optional<Review> findByIdAndDeletedFalse(UUID id);

	Page<Review> findByUserIdAndDeletedFalse(UUID id, Pageable pageable);

	Page<Review> findAllByKeywordAndDeletedFalse(Keyword keyword, Pageable pageable);

	Page<Review> findAllByActivityTypeAndDeletedFalse(ActivityType activityType, Pageable pageable);

	Page<Review> findAllByKeywordAndActivityTypeAndDeletedFalse(
		Keyword keyword,
		ActivityType activityType,
		Pageable pageable);

	List<Review> findAllByUserIdAndDeletedFalse(UUID userId);

	@Modifying
	@Query("UPDATE reviews r SET r.likeCount = r.likeCount + 1 WHERE r.id = :reviewId")
	void incrementLikeCount(@Param("reviewId") UUID reviewId);

	@Modifying
	@Query("UPDATE reviews r SET r.likeCount = r.likeCount - 1 WHERE r.id = :reviewId AND r.likeCount > 0")
	void decrementLikeCount(@Param("reviewId") UUID reviewId);

	@Modifying
	@Transactional
	@Query("UPDATE reviews r SET r.awardOcrResult = :awardOcrResult WHERE r.id = :reviewId")
	void updateAwardOcrResult(
		@Param("reviewId") UUID reviewId,
		@Param("awardOcrResult") boolean awardOcrResult
	);

	@Modifying
	@Transactional
	@Query("UPDATE reviews r SET r.ocrResult = :ocrResult WHERE r.id = :reviewId")
	void updateOcrResult(
		@Param("reviewId") UUID reviewId,
		@Param("ocrResult") boolean ocrResult
	);
}

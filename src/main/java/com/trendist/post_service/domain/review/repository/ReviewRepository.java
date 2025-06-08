package com.trendist.post_service.domain.review.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.dto.response.ReviewMonthlyCountResponse;

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

	List<Review> findAllByUpdatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Modifying
	@Query("UPDATE reviews r SET r.likeCount = r.likeCount + 1 WHERE r.id = :reviewId")
	void incrementLikeCount(@Param("reviewId") UUID reviewId);

	@Modifying
	@Query("UPDATE reviews r SET r.likeCount = r.likeCount - 1 WHERE r.id = :reviewId AND r.likeCount > 0")
	void decrementLikeCount(@Param("reviewId") UUID reviewId);

	@Query("""
		SELECT new com.trendist.post_service.domain.review.dto.response.ReviewMonthlyCountResponse(
		  YEAR(r.activityEndDate), MONTH(r.activityEndDate), COUNT(r)
		)
		FROM reviews r
		WHERE r.userId = :userId
		  AND r.deleted = false
		GROUP BY YEAR(r.activityEndDate), MONTH(r.activityEndDate)
		ORDER BY YEAR(r.activityEndDate), MONTH(r.activityEndDate)
		""")
	List<ReviewMonthlyCountResponse> countMonthlyByUserIdByEndDate(@Param("userId") UUID userId);

}

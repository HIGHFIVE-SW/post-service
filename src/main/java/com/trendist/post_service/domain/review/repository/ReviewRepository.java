package com.trendist.post_service.domain.review.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.domain.review.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
	Page<Review> findAllByDeletedFalse(Pageable pageable);

	Optional<Review> findByIdAndDeletedFalse(UUID id);

	Page<Review> findByUserIdAndDeletedFalse(UUID id, Pageable pageable);
}

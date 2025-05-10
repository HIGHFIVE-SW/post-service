package com.trendist.post_service.domain.post.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.domain.post.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
	Optional<PostLike> findByPostIdAndUserId(UUID postId, UUID userId);

	boolean existsByPostIdAndUserId(UUID postId, UUID userId);

	void deleteByPostIdAndUserId(UUID postId, UUID userId);
}

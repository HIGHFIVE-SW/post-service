package com.trendist.post_service.domain.post.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trendist.post_service.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
	Page<Post> findAllByDeletedFalse(Pageable pageable);

	Optional<Post> findByIdAndDeletedFalse(UUID postId);

	Page<Post> findByUserIdAndDeletedFalse(UUID userId, Pageable pageable);

	@Modifying
	@Query("UPDATE posts p SET p.likeCount = p.likeCount + 1 WHERE p.id = :postId")
	void incrementLikeCount(@Param("postId") UUID postId);

	@Modifying
	@Query("UPDATE posts p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId AND p.likeCount > 0")
	void decrementLikeCount(@Param("postId") UUID postId);
}

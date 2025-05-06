package com.trendist.post_service.post.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trendist.post_service.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, UUID> {
}

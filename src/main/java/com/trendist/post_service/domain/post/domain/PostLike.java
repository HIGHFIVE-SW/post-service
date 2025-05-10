package com.trendist.post_service.domain.post.domain;

import java.util.UUID;

import com.trendist.post_service.global.common.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
	name = "post_likes",
	uniqueConstraints = @UniqueConstraint(columnNames = {"post_id","user_id"})
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLike extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "post_id")
	private UUID postId;

	@Column(name = "user_id")
	private UUID userId;

	public PostLike(UUID postId, UUID userId){
		this.postId = postId;
		this.userId = userId;
	}
}

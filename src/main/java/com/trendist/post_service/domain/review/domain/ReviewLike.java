package com.trendist.post_service.domain.review.domain;

import java.util.UUID;

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
	name = "review_likes",
	uniqueConstraints = @UniqueConstraint(columnNames = {"review_id","user_id"})
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "review_id")
	private UUID reviewId;

	@Column(name = "user_id")
	private UUID userId;

	public ReviewLike(UUID reviewId, UUID userId){
		this.reviewId = reviewId;
		this.userId = userId;
	}
}

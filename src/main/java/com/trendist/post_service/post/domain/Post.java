package com.trendist.post_service.post.domain;

import java.util.Set;
import java.util.UUID;

import com.trendist.post_service.global.common.domain.BaseTimeEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "post_id")
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "post_title")
	private String title;

	@Column(name = "post_content")
	private String content;

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "post_keywords", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "keyword")
	private Set<Keyword> keywords;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "deleted")
	@Builder.Default
	private Boolean deleted = false;
}

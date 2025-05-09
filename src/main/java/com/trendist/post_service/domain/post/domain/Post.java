package com.trendist.post_service.domain.post.domain;

import java.util.List;
import java.util.UUID;

import com.trendist.post_service.global.common.domain.BaseTimeEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "post_title")
	private String title;

	@Column(name = "post_content")
	private String content;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "image_urls")
	private List<String> imageUrls;

	@Column(name = "post_like_count")
	@Builder.Default
	private Integer likeCount = 0;

	@Column(name = "deleted")
	@Builder.Default
	private Boolean deleted = false;
}

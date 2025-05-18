package com.trendist.post_service.domain.review.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Builder;
import lombok.Getter;

@Document(indexName = "mysql-trendist.trendist.review_image_urls")
@Builder
@Getter
public class ReviewImageDocument {
	@Id
	private String id;

	@Field(name = "review_id", type = FieldType.Keyword)
	private String reviewId;

	@Field(name = "image_urls", type = FieldType.Keyword)
	private String imageUrls;
}

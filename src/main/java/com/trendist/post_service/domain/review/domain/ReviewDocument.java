package com.trendist.post_service.domain.review.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Document(indexName = "mysql-trendist.trendist.reviews")
@Builder
@Getter
public class ReviewDocument {
	@Id
	private String id;

	@MultiField(
		mainField = @Field(name = "review_title", type = FieldType.Text, analyzer = "standard"),
		otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword)
		}
	)
	private String title;

	@Field(name = "review_content", type = FieldType.Keyword)
	private String content;

	@Field(name = "review_keyword", type = FieldType.Keyword)
	private String keyword;

	@Field(name = "nickname", type = FieldType.Keyword)
	private String nickname;

	@Field(name = "review_like_count", type = FieldType.Integer)
	private Integer likeCount;

	@Field(name = "created_at", type = FieldType.Keyword)
	private String createdAt;
}

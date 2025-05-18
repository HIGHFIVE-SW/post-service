package com.trendist.post_service.domain.post.domain;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Document(indexName = "mysql-trendist.trendist.posts")
@Builder
@Getter
public class PostDocument {
	@Id
	private String id;

	@MultiField(
		mainField = @Field(name = "post_title", type = FieldType.Text, analyzer = "standard"),
		otherFields = {
			@InnerField(suffix = "keyword", type = FieldType.Keyword)
		}
	)
	private String title;

	@Field(name = "nickname", type = FieldType.Keyword)
	private String nickname;

	@Field(name = "post_like_count", type = FieldType.Integer)
	private Integer likeCount;

	@Field(name = "created_at", type = FieldType.Keyword)
	private String createdAt;
}

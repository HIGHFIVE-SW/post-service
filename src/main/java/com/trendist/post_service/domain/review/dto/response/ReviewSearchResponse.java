package com.trendist.post_service.domain.review.dto.response;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.domain.review.domain.ReviewDocument;

import lombok.Builder;

@Builder
public record ReviewSearchResponse(
	UUID id,
	String title,
	String content,
	String keyword,
	String nickname,
	Integer likeCount,
	String createdAt,
	List<String> reviewImageUrls
) {
	public static ReviewSearchResponse of(ReviewDocument reviewDocument, List<String> reviewImageUrls) {
		byte[] bytes = Base64.getDecoder().decode(reviewDocument.getId());
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		UUID uuid = new UUID(bb.getLong(), bb.getLong());

		return ReviewSearchResponse.builder()
			.id(uuid)
			.title(reviewDocument.getTitle())
			.content(reviewDocument.getContent())
			.keyword(reviewDocument.getKeyword())
			.nickname(reviewDocument.getNickname())
			.likeCount(reviewDocument.getLikeCount())
			.createdAt(reviewDocument.getCreatedAt())
			.reviewImageUrls(reviewImageUrls)
			.build();
	}
}

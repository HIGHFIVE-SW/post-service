package com.trendist.post_service.domain.post.dto.response;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.PostDocument;

import lombok.Builder;

@Builder
public record PostGetSearchResponse(
	UUID id,
	String title,
	String nickname,
	Integer likeCount,
	String createdAt
) {
	public static PostGetSearchResponse from(PostDocument postDocument) {
		byte[] bytes = Base64.getDecoder().decode(postDocument.getId());
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		UUID uuid = new UUID(bb.getLong(), bb.getLong());

		return PostGetSearchResponse.builder()
			.id(uuid)
			.title(postDocument.getTitle())
			.nickname(postDocument.getNickname())
			.likeCount(postDocument.getLikeCount())
			.createdAt(postDocument.getCreatedAt())
			.build();
	}
}

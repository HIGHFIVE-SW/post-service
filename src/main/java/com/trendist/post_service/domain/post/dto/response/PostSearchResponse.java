package com.trendist.post_service.domain.post.dto.response;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

import com.trendist.post_service.domain.post.domain.PostDocument;

import lombok.Builder;

@Builder
public record PostSearchResponse(
	UUID id,
	String title,
	String nickname,
	Integer likeCount,
	String createdAt
) {
	public static PostSearchResponse from(PostDocument postDocument) {
		byte[] bytes = Base64.getDecoder().decode(postDocument.getId());
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		UUID uuid = new UUID(bb.getLong(), bb.getLong());

		return PostSearchResponse.builder()
			.id(uuid)
			.title(postDocument.getTitle())
			.nickname(postDocument.getNickname())
			.likeCount(postDocument.getLikeCount())
			.createdAt(postDocument.getCreatedAt())
			.build();
	}
}

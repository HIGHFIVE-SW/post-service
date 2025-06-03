package com.trendist.post_service.global.feign.user.dto.response;

import java.util.UUID;

public record UserUpdateExpResponse(
	UUID id,
	int exp
) {
}

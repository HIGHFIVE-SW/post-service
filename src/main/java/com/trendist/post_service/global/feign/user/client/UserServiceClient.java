package com.trendist.post_service.global.feign.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.trendist.post_service.global.feign.user.dto.UserProfileResponse;
import com.trendist.post_service.global.response.ApiResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {
	@GetMapping("/users/profile")
	ApiResponse<UserProfileResponse> getUserProfile(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
	);
}

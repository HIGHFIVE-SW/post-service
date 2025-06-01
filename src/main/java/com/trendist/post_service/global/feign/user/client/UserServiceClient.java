package com.trendist.post_service.global.feign.user.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.trendist.post_service.global.feign.user.dto.response.UserProfileResponse;
import com.trendist.post_service.global.feign.user.dto.response.UserUpdateExpResponse;
import com.trendist.post_service.global.response.ApiResponse;

@FeignClient(name = "user-service")
public interface UserServiceClient {

	@GetMapping("/users/profile")
	ApiResponse<UserProfileResponse> getMyProfile(
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
	);

	@GetMapping("/users/profile/{userId}")
	ApiResponse<UserProfileResponse> getUserProfile(
		@PathVariable(name = "userId") UUID userId
	);

	@PatchMapping("/users/{userId}/exp")
	ApiResponse<UserUpdateExpResponse> updateUserExp(
		@PathVariable("userId") UUID userId,
		@RequestBody int expToAdd
	);
}

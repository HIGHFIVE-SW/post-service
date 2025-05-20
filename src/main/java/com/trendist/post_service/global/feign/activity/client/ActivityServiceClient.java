package com.trendist.post_service.global.feign.activity.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.trendist.post_service.global.feign.activity.dto.ActivityGetResponse;
import com.trendist.post_service.global.response.ApiResponse;

@FeignClient(name = "activity-service")
public interface ActivityServiceClient {
	@GetMapping("/activities/{activityId}")
	ApiResponse<ActivityGetResponse> getActivity(
		@PathVariable(name = "activityId") UUID activityId
	);
}

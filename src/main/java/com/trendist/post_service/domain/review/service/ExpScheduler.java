package com.trendist.post_service.domain.review.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.repository.ReviewRepository;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.global.feign.user.dto.request.UserExpRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExpScheduler {
	private final ReviewService reviewService;
	private final UserServiceClient userServiceClient;
	private final ReviewRepository reviewRepository;

	@Scheduled(cron = "0 30 1 * * *")
	@Transactional
	public void assignDailyPoints() {
		LocalDateTime end = LocalDate.now().atStartOfDay();
		LocalDateTime start = end.minusDays(1);

		List<Review> reviews = reviewRepository.findAllByUpdatedAtBetween(start, end);
		Map<UUID, Integer> expByUser = new HashMap<>();

		for (Review review : reviews) {
			int exp = calculateExp(review);
			if (exp > 0) {
				expByUser.merge(review.getUserId(), exp, Integer::sum);
			}
		}
		// user service로 exp 전송
		for (Map.Entry<UUID, Integer> e : expByUser.entrySet()) {
			UUID userId = e.getKey();
			int totalExp = e.getValue();
			userServiceClient.updateUserExp(
				userId,
				new UserExpRequest(totalExp)
			);
		}
	}

	private int calculateExp(Review review) {
		//OCR 결과 false 라면 경험치 미지급
		if (Boolean.FALSE.equals(review.getOcrResult())) {
			return 0;
		}
		//공모전은 awardOcrResult에 따라 300 / 100점 지급
		if (review.getActivityType() == ActivityType.CONTEST) {
			return Boolean.TRUE.equals(review.getAwardOcrResult()) ? 300 : 100;
		}
		//기타 활동들은 기간에 따라 차등 지급
		return switch (review.getActivityPeriod()) {
			case OneDay -> 10;
			case OneWeek -> 50;
			case OneMonth -> 200;
			case WithinSixMonths -> 500;
			case OverSixMonths -> 1000;
		};
	}
}

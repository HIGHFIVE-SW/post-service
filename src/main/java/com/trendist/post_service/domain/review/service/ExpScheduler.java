package com.trendist.post_service.domain.review.service;

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

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExpScheduler {
	private final UserServiceClient userServiceClient;
	private final ReviewRepository reviewRepository;

	// 5:30 부터 6시간 간격
	@Scheduled(cron = "0 5/10 * * * *")
	@Transactional
	public void assignDailyPoints() {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusHours(6);

		List<Review> reviews = reviewRepository.findAllByUpdatedAtBetween(start, end);
		Map<UUID, Integer> expByUser = new HashMap<>();

		for (Review review : reviews) {
			int exp = calculateExp(review);
			if (exp > 0) {
				expByUser.merge(review.getUserId(), exp, Integer::sum);
			}
		}
		// user service로 exp 전송
		for (var entry : expByUser.entrySet()) {
			UUID userId = entry.getKey();
			int totalExp = entry.getValue();
			userServiceClient.updateUserExp(userId, totalExp);
		}
	}

	private int calculateExp(Review review) {
		//OCR 결과 false 라면 경험치 미지급
		if (!review.getOcrResult()) {
			return 0;
		}
		//공모전 로직
		//awardOcrResult 가 Boolean TRUE 인 경우만 won=true
		if (review.getActivityType() == ActivityType.CONTEST) {
			int state = review.getIsExpGiven();
			boolean won = Boolean.TRUE.equals(review.getAwardOcrResult());

			if (state == 0) {
				//공모전은 awardOcrResult에 따라 300 / 100점 지급
				int firstAward = won ? 300 : 100;
				review.setIsExpGiven(won ? 2 : 1);
				return firstAward;
			}

			//수상사진을 처음에 올리지 못하고 뒤늦게 글을 수정하여 올린 경우 ->  추가적으로 200점 획득
			if (state == 1 && won) {
				review.setIsExpGiven(2);
				return 200;
			}

			//state=2인 경우는 공모전으로 300점까지 획득한 경우 -> 더이상 보상 없음
			return 0;
		}

		//공모전이 아닌 경우, 경험치는 오직 1번만 수령 가능
		if (review.getIsExpGiven() != 0) {
			return 0;
		}

		//기타 활동들은 기간에 따라 차등 지급
		int exp;
		switch (review.getActivityPeriod()) {
			case OneDay -> exp = 10;
			case OneWeek -> exp = 50;
			case OneMonth -> exp = 200;
			case WithinSixMonths -> exp = 500;
			case OverSixMonths -> exp = 1000;
			default -> exp = 0;
		}
		review.setIsExpGiven(1);
		return exp;
	}
}

package com.trendist.post_service.domain.review.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.trendist.post_service.domain.review.domain.ActivityType;
import com.trendist.post_service.domain.review.domain.Keyword;
import com.trendist.post_service.domain.review.domain.Review;
import com.trendist.post_service.domain.review.domain.ReviewDocument;
import com.trendist.post_service.domain.review.domain.ReviewImageDocument;
import com.trendist.post_service.domain.review.domain.ReviewLike;
import com.trendist.post_service.domain.review.domain.ReviewSort;
import com.trendist.post_service.domain.review.dto.request.ReviewActivityCreateRequest;
import com.trendist.post_service.domain.review.dto.request.ReviewCreateRequest;
import com.trendist.post_service.domain.review.dto.request.ReviewUpdateRequest;
import com.trendist.post_service.domain.review.dto.response.OcrResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewCreateResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewDeleteResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetAllResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewGetResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewLikeResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewSearchResponse;
import com.trendist.post_service.domain.review.dto.response.ReviewUpdateResponse;
import com.trendist.post_service.domain.review.repository.ReviewLikeRepository;
import com.trendist.post_service.domain.review.repository.ReviewRepository;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.feign.activity.client.ActivityServiceClient;
import com.trendist.post_service.global.feign.activity.dto.ActivityGetResponse;
import com.trendist.post_service.global.feign.user.client.UserServiceClient;
import com.trendist.post_service.global.response.status.ErrorStatus;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final ReviewLikeRepository reviewLikeRepository;
	private final UserServiceClient userServiceClient;
	private final ActivityServiceClient activityServiceClient;
	private final ElasticsearchOperations esOps;
	private final RestTemplate restTemplate;

	// OCR flask 서버 기본 URL
	@Value("$review.ocr.base-url")
	private String ocrBaseUrl;

	//OCR 검사 호출 메서드
	private OcrResponse callOcrServer(UUID reviewId) {
		String url = ocrBaseUrl + "/ocr/" + reviewId;
		ResponseEntity<OcrResponse> response = restTemplate.getForEntity(url, OcrResponse.class);
		if (response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
			throw new ApiException(ErrorStatus._OCR_PROCESSING_FAILED);
		}
		return response.getBody();
	}

	@Transactional
	public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		String nickname = userServiceClient.getMyProfile("").getResult().nickname();

		//review 작성 시 이미지 최소 1장 필수
		if (reviewCreateRequest.imageUrls() == null || reviewCreateRequest.imageUrls().isEmpty()) {
			throw new ApiException(ErrorStatus._REVIEW_IMAGE_REQUIRED);
		}

		Review review = Review.builder()
			.title(reviewCreateRequest.title())
			.keyword(reviewCreateRequest.keyword())
			.activityType(reviewCreateRequest.activityType())
			.activityPeriod(reviewCreateRequest.activityPeriod())
			.activityEndDate(reviewCreateRequest.activityEndDate())
			.activityName(reviewCreateRequest.activityName())
			.content(reviewCreateRequest.content())
			.awardImageUrl(reviewCreateRequest.awardImageUrl())
			.imageUrls(reviewCreateRequest.imageUrls())
			.userId(userId)
			.nickname(nickname)
			.build();

		reviewRepository.save(review);

		// save 이후 reviewId 생성됨, imageUrl 저장됨
		UUID reviewId = review.getId();

		// flask OCR 요청
		OcrResponse ocrResponse = callOcrServer(reviewId);

		// Award Img ocr 결과 반영
		boolean isAwardImgOk = Boolean.parseBoolean(ocrResponse.getAwardOcrResult());
		review.setAwardOcrResult(isAwardImgOk);
		reviewRepository.updateAwardOcrResult(reviewId, isAwardImgOk);

		// review img ocr 결과 반영
		boolean isReviewImgOk = Boolean.parseBoolean(ocrResponse.getOcrResult());
		review.setOcrResult(isReviewImgOk);
		reviewRepository.updateOcrResult(reviewId, isReviewImgOk);

		return ReviewCreateResponse.from(review);
	}

	@Transactional
	public ReviewCreateResponse createActivityReview(
		UUID activityId,
		ReviewActivityCreateRequest request
	) {
		if (request.imageUrls() == null || request.imageUrls().isEmpty()) {
			throw new ApiException(ErrorStatus._REVIEW_IMAGE_REQUIRED);
		}

		UUID userId = userServiceClient.getMyProfile("").getResult().id();
		String nickname = userServiceClient.getMyProfile("").getResult().nickname();

		ActivityGetResponse activity = activityServiceClient.getActivity(activityId).getResult();

		Review review = Review.builder()
			.activityId(activityId)
			.title(request.title())
			.keyword(activity.keyword())
			.activityType(activity.activityType())
			.activityPeriod(request.activityPeriod())
			.activityEndDate(activity.endDate().toLocalDate())
			.activityName(activity.name())
			.content(request.content())
			.awardImageUrl(request.awardImageUrl())
			.imageUrls(request.imageUrls())
			.userId(userId)
			.nickname(nickname)
			.build();

		reviewRepository.save(review);

		// save 이후 reviewId 생성됨, imageUrl 저장됨
		UUID reviewId = review.getId();

		// flask OCR 요청
		OcrResponse ocrResponse = callOcrServer(reviewId);

		// Award Img ocr 결과 반영
		boolean isAwardImgOk = Boolean.parseBoolean(ocrResponse.getAwardOcrResult());
		review.setAwardOcrResult(isAwardImgOk);
		reviewRepository.updateAwardOcrResult(reviewId, isAwardImgOk);

		// review img ocr 결과 반영
		boolean isReviewImgOk = Boolean.parseBoolean(ocrResponse.getOcrResult());
		review.setOcrResult(isReviewImgOk);
		reviewRepository.updateOcrResult(reviewId, isReviewImgOk);

		return ReviewCreateResponse.from(review);
	}

	@Transactional
	public ReviewUpdateResponse updateReview(UUID reviewId, ReviewUpdateRequest reviewUpdateRequest) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));

		UUID nowUserId = userServiceClient.getMyProfile("").getResult().id();
		if (!nowUserId.equals(review.getUserId())) {
			throw new ApiException(ErrorStatus._REVIEW_UPDATE_FORBIDDEN);
		}

		//review 작성 시 이미지 최소 1장 필수
		if (reviewUpdateRequest.imageUrls() == null || reviewUpdateRequest.imageUrls().isEmpty()) {
			throw new ApiException(ErrorStatus._REVIEW_IMAGE_REQUIRED);
		}

		review.setTitle(reviewUpdateRequest.title());
		review.setContent(reviewUpdateRequest.content());
		review.setAwardImageUrl(reviewUpdateRequest.awardImageUrl());
		review.setImageUrls(reviewUpdateRequest.imageUrls());

		reviewRepository.save(review);

		// flask OCR 요청
		OcrResponse ocrResponse = callOcrServer(reviewId);

		// Award Img ocr 결과 반영
		boolean isAwardImgOk = Boolean.parseBoolean(ocrResponse.getAwardOcrResult());
		review.setAwardOcrResult(isAwardImgOk);
		reviewRepository.updateAwardOcrResult(reviewId, isAwardImgOk);

		// review img ocr 결과 반영
		boolean isReviewImgOk = Boolean.parseBoolean(ocrResponse.getOcrResult());
		review.setOcrResult(isReviewImgOk);
		reviewRepository.updateOcrResult(reviewId, isReviewImgOk);

		return ReviewUpdateResponse.from(review);
	}

	@Transactional
	public ReviewDeleteResponse deleteReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));

		UUID nowUserId = userServiceClient.getMyProfile("").getResult().id();
		if (!nowUserId.equals(review.getUserId())) {
			throw new ApiException(ErrorStatus._REVIEW_DELETE_FORBIDDEN);
		}

		review.setDeleted(true);

		reviewRepository.save(review);

		return ReviewDeleteResponse.from(review);
	}

	public Page<ReviewGetAllResponse> getReviews(
		Keyword keyword,
		ActivityType activityType,
		ReviewSort reviewSort,
		int page) {
		Sort sort = switch (reviewSort) {
			case LIKES -> Sort.by("likeCount").descending();
			case RECENT -> Sort.by("createdAt").descending();
		};

		Pageable pageable = PageRequest.of(page, 9, sort);

		if (keyword != null && activityType != null) {
			return reviewRepository.findAllByKeywordAndActivityTypeAndDeletedFalse(keyword, activityType, pageable)
				.map(ReviewGetAllResponse::from);
		} else if (keyword != null) {
			return reviewRepository.findAllByKeywordAndDeletedFalse(keyword, pageable)
				.map(ReviewGetAllResponse::from);
		} else if (activityType != null) {
			return reviewRepository.findAllByActivityTypeAndDeletedFalse(activityType, pageable)
				.map(ReviewGetAllResponse::from);
		} else {
			return reviewRepository
				.findAllByDeletedFalse(pageable)
				.map(ReviewGetAllResponse::from);
		}
	}

	public ReviewGetResponse getReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
			.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_NOT_FOUND));
		String profileUrl = userServiceClient.getUserProfile(review.getUserId()).getResult().profileUrl();

		return ReviewGetResponse.of(review, profileUrl);
	}

	@Transactional
	public ReviewLikeResponse likeReview(UUID reviewId) {
		UUID userId = userServiceClient.getMyProfile("").getResult().id();

		ReviewLike reviewLike;
		boolean like;

		if (!reviewLikeRepository.existsByReviewIdAndUserId(reviewId, userId)) {
			reviewLike = ReviewLike.builder()
				.reviewId(reviewId)
				.userId(userId)
				.build();
			reviewLikeRepository.save(reviewLike);
			reviewRepository.incrementLikeCount(reviewId);
			like = true;
		} else {
			reviewLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId)
				.orElseThrow(() -> new ApiException(ErrorStatus._REVIEW_LIKE_NOT_FOUND));
			reviewLikeRepository.deleteByReviewIdAndUserId(reviewId, userId);
			reviewRepository.decrementLikeCount(reviewId);
			like = false;
		}

		return ReviewLikeResponse.of(reviewLike, like);
	}

	public Page<ReviewSearchResponse> searchReviews(String keyword, int page) {
		Pageable pageable = PageRequest.of(page, 9);

		NativeQuery reviewQuery = NativeQuery.builder()
			.withQuery(q -> q
				.wildcard(w -> w
					.field("review_title.keyword")
					.value("*" + keyword + "*")
				)
			)
			.withSort(s -> s
				.field(f -> f
					.field("created_at.keyword")
					.order(SortOrder.Desc)
				)
			)
			.withPageable(pageable)
			.build();

		SearchHits<ReviewDocument> reviewHits = esOps.search(reviewQuery, ReviewDocument.class);
		List<String> reviewIds = reviewHits.getSearchHits().stream()
			.map(hit -> hit.getContent().getId())
			.toList();

		List<FieldValue> idValues = reviewIds.stream()
			.map(FieldValue::of)
			.toList();

		// 4) review_image_urls 인덱스에서 review_id 필드로 terms 쿼리
		NativeQuery imageQuery = NativeQuery.builder()
			.withQuery(q -> q.terms(t -> t
				.field("review_id.keyword")
				.terms(
					TermsQueryField.of(b -> b.value(idValues))
				)
			))
			.withPageable(Pageable.unpaged())
			.build();

		SearchHits<ReviewImageDocument> imageHits = esOps.search(imageQuery, ReviewImageDocument.class);

		Map<String, List<String>> imageMap = imageHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.groupingBy(
				ReviewImageDocument::getReviewId,
				Collectors.mapping(ReviewImageDocument::getImageUrls, Collectors.toList())
			));

		List<ReviewSearchResponse> content = reviewHits.getSearchHits().stream()
			.map(hit -> {
				ReviewDocument doc = hit.getContent();
				List<String> urls = imageMap.getOrDefault(doc.getId(), Collections.emptyList());
				return ReviewSearchResponse.of(doc, urls);
			})
			.toList();

		return new PageImpl<>(content, pageable, reviewHits.getTotalHits());
	}
}

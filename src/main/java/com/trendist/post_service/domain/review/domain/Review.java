package com.trendist.post_service.domain.review.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.trendist.post_service.global.common.domain.BaseTimeEntity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "reviews")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "review_id")
	private UUID id;

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "activity_id")
	private UUID activityId;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "review_title")
	private String title;

	//review 작성 시 keyword 1개 선택
	@Enumerated(EnumType.STRING)
	@Column(name = "review_keyword")
	private Keyword keyword;

	//review 작성 시 ActivityType 1개 선택
	@Enumerated(EnumType.STRING)
	@Column(name = "review_activity_type")
	private ActivityType activityType;

	//review 작성 시 활동 기간 1개 선택
	@Enumerated(EnumType.STRING)
	@Column(name = "activity_period")
	private ActivityPeriod activityPeriod;

	//review 작성 시 활종 종료일 선택
	@Column(name = "activity_end_date")
	private LocalDate activityEndDate;

	//review 작성 참여 활동 이름 입력받음
	@Column(name = "activity_name")
	private String activityName;

	@Column(name = "review_content")
	private String content;

	@Column(name = "award_image_url")
	private String awardImageUrl;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "review_image_urls", joinColumns = @JoinColumn(name = "review_id"))
	@Column(name = "image_urls")
	private List<String> imageUrls;

	@Column(name = "review_like_count")
	@Builder.Default
	private Integer likeCount = 0;

	@Column(name = "award_ocr_result")
	private Boolean awardOcrResult = false;

	@Column(name = "ocr_result")
	private Boolean ocrResult = false;

	@Column(name = "deleted")
	@Builder.Default
	private Boolean deleted = false;
}

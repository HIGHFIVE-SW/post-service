package com.trendist.post_service.domain.s3.service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.trendist.post_service.global.exception.ApiException;
import com.trendist.post_service.global.response.status.ErrorStatus;
import com.trendist.post_service.domain.s3.dto.response.PresignedUrlResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Value("${aws.s3.prefix}")
	private String prefix;

	private static final int MAX_FILES_COUNT = 5;

	@Transactional
	public PresignedUrlResponse getPreSignedUrl(List<String> originalFilenames) {
		if (originalFilenames.size() > MAX_FILES_COUNT) {
			throw new ApiException(ErrorStatus._S3_OVER_MAX_FILES);
		}

		List<String> urls = originalFilenames.stream()
			.map(this::createPath)
			.map(path -> {
				GeneratePresignedUrlRequest request = getGeneratePreSignedUrlRequest(bucketName, path);
				URL presignedUrl = amazonS3.generatePresignedUrl(request);
				return presignedUrl.toString();
			})
			.toList();

		return PresignedUrlResponse.from(urls);
	}

	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {

		return new GeneratePresignedUrlRequest(bucket, fileName)
			.withMethod(HttpMethod.PUT)
			.withExpiration(getPreSignedUrlExpiration());
	}

	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 2;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

	private String createFileId() {
		return UUID.randomUUID().toString();
	}

	private String createPath(String fileName) {
		String fileId = createFileId();
		return String.format("%s/%s", prefix, fileId + fileName);
	}
}

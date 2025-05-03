package com.trendist.post_service.s3.service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.trendist.post_service.s3.dto.response.PresignedUrlResponse;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresignedUrlService {
	private final S3Template s3Template;
	private final AmazonS3 amazonS3;
	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Value("ap-northeast-2")
	private String region;

	@Transactional
	public PresignedUrlResponse getPreSignedUrl(String originalFilename) {
		String fileName = createPath(originalFilename);
		GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucketName, fileName);
		URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

		return PresignedUrlResponse.from(presignedUrl.toString());
	}

	private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {

		return new GeneratePresignedUrlRequest(bucket, fileName).withMethod(
			HttpMethod.PUT).withExpiration(getPreSignedUrlExpiration());
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
		return String.format("%s", fileId + fileName);
	}
}

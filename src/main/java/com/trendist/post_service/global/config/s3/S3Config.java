package com.trendist.post_service.global.config.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	@Value("${aws.s3.accessKey}")
	private String accessKey;

	@Value("${aws.s3.secretKey}")
	private String secretKey;

	@Value("${aws.s3.region}")
	private String region;

	@Bean
	@Primary
	public BasicAWSCredentials awsCredentialsProvider(){
		return new BasicAWSCredentials(accessKey,secretKey);
	}

	@Bean
	public AmazonS3 amazonS3(){
		return AmazonS3ClientBuilder.standard()
			.withRegion(region)
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider()))
			.build();
	}
}

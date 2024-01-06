package com.cantcode.yt.filemanagement.webapp.configs;

import com.cantcode.yt.filemanagement.webapp.configs.properties.S3BucketProperties;
import com.cantcode.yt.filemanagement.webapp.configs.properties.S3Properties;
import com.cantcode.yt.filemanagement.webapp.exceptions.BucketCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client(final S3Properties s3Properties,
                             final S3BucketProperties s3BucketProperties) throws URISyntaxException {
        final AwsCredentials awsCredentials = AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey());

        S3Client s3Client = S3Client.builder()
                .endpointOverride(new URI(s3Properties.getUrl()))
                .forcePathStyle(true)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.AP_SOUTH_1)
                .build();

        createBucketsIfNotExisting(s3BucketProperties, s3Client);

        return s3Client;
    }

    private void createBucketsIfNotExisting(final S3BucketProperties s3BucketProperties, final S3Client s3Client) {
        List<String> buckets = List.of(s3BucketProperties.getRawVideos(), s3BucketProperties.getTranscodedVideos());
        List<String> bucketsNotFound = s3Client.listBuckets()
                .buckets()
                .stream()
                .map(Bucket::name)
                .toList();

        buckets.stream()
                .filter(bucketName -> !bucketsNotFound.contains(bucketName))
                .forEach(bucketName -> {
                    try {
                        s3Client.createBucket(CreateBucketRequest.builder()
                                .bucket(bucketName)
                                .build());
                    } catch (Exception e) {
                        throw new BucketCreationException("Error creating buckets", e);
                    }
                });
    }
}

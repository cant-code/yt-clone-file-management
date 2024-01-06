package com.cantcode.yt.filemanagement.webapp.configs;

import com.cantcode.yt.filemanagement.webapp.configs.properties.S3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client(final S3Properties s3Properties) throws URISyntaxException {
        final AwsCredentials awsCredentials = AwsBasicCredentials.create(s3Properties.getAccessKey(), s3Properties.getSecretKey());

        return S3Client.builder()
                .endpointOverride(new URI(s3Properties.getUrl()))
                .forcePathStyle(true)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.AP_SOUTH_1)
                .build();
    }
}

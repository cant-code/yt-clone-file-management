package com.cantcode.yt.filemanagement.webapp.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3.buckets")
public class S3BucketProperties {

    private String rawVideos;
    private String transcodedVideos;

    public String getRawVideos() {
        return rawVideos;
    }

    public void setRawVideos(String rawVideos) {
        this.rawVideos = rawVideos;
    }

    public String getTranscodedVideos() {
        return transcodedVideos;
    }

    public void setTranscodedVideos(String transcodedVideos) {
        this.transcodedVideos = transcodedVideos;
    }
}

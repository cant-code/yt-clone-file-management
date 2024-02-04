package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.service.spi.S3Service;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    public S3ServiceImpl(final S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public void putObject(final PutObjectRequest request, final RequestBody requestBody) {
        s3Client.putObject(request,requestBody);
    }

    @Override
    public ResponseInputStream<GetObjectResponse> getObject(final GetObjectRequest request) {
        return s3Client.getObject(request);
    }
}

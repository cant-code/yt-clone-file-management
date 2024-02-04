package com.cantcode.yt.filemanagement.webapp.service.spi;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public interface S3Service {

    void putObject(PutObjectRequest request, RequestBody requestBody);

    ResponseInputStream<GetObjectResponse> getObject(GetObjectRequest request);
}

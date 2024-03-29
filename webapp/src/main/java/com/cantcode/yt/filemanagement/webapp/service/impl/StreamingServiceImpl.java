package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.configs.properties.S3BucketProperties;
import com.cantcode.yt.filemanagement.webapp.exceptions.GeneralBadRequestException;
import com.cantcode.yt.filemanagement.webapp.model.StreamBodyResponse;
import com.cantcode.yt.filemanagement.webapp.repository.EncodedVideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.entities.EncodedVideo;
import com.cantcode.yt.filemanagement.webapp.service.spi.S3Service;
import com.cantcode.yt.filemanagement.webapp.service.spi.StreamingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import static com.cantcode.yt.filemanagement.webapp.utils.StreamingConstants.ACCEPTED_QUALITY_PARAM;

@Service
public class StreamingServiceImpl implements StreamingService {

    private final S3Service s3Service;
    private final EncodedVideosRepository encodedVideosRepository;
    private final S3BucketProperties s3BucketProperties;

    public StreamingServiceImpl(final S3Service s3Service,
                                final EncodedVideosRepository encodedVideosRepository,
                                final S3BucketProperties s3BucketProperties) {
        this.s3Service = s3Service;
        this.encodedVideosRepository = encodedVideosRepository;
        this.s3BucketProperties = s3BucketProperties;
    }

    @Override
    @Transactional(readOnly = true)
    public StreamBodyResponse streamVideo(final Long fileId, final String quality, final String range) {
        if (!ACCEPTED_QUALITY_PARAM.contains(quality)) {
            throw new GeneralBadRequestException("Invalid quality. Should be one of: %s".formatted(ACCEPTED_QUALITY_PARAM));
        }
        final EncodedVideo encodedVideo = encodedVideosRepository.findByVideoIdAndQuality(fileId, quality)
                .orElseThrow(() -> new GeneralBadRequestException("File with given id not found"));
        final GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3BucketProperties.getTranscodedVideos())
                .key(encodedVideo.getLink())
                .range(range)
                .build();
        final ResponseInputStream<GetObjectResponse> object = s3Service.getObject(request);
        return new StreamBodyResponse(object::transferTo, encodedVideo.getSize());
    }
}

package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.configs.properties.S3BucketProperties;
import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;
import com.cantcode.yt.filemanagement.webapp.exceptions.FileUploadException;
import com.cantcode.yt.filemanagement.webapp.model.UploadVideoRequest;
import com.cantcode.yt.filemanagement.webapp.repository.RawVideoRepository;
import com.cantcode.yt.filemanagement.webapp.repository.VideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.entities.RawVideo;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;
    private final VideosRepository videosRepository;
    private final RawVideoRepository rawVideoRepository;
    private final S3BucketProperties s3BucketProperties;

    public FileServiceImpl(final S3Client s3Client,
                           final VideosRepository videosRepository,
                           final RawVideoRepository rawVideoRepository,
                           final S3BucketProperties s3BucketProperties) {
        this.s3Client = s3Client;
        this.videosRepository = videosRepository;
        this.rawVideoRepository = rawVideoRepository;
        this.s3BucketProperties = s3BucketProperties;
    }

    @Override
    @Transactional
    public void uploadFile(final String userId, final MultipartFile multipartFile, final UploadVideoRequest videoRequest) {
        try {
            final String fileName = generateFileName(multipartFile.getName());
            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3BucketProperties.getRawVideos())
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(multipartFile.getBytes()));

            final Videos video = createVideo(userId, multipartFile, videoRequest);
            videosRepository.save(video);

            final RawVideo rawVideo = createRawVideo(multipartFile, video, fileName);
            rawVideoRepository.save(rawVideo);

            //TODO: Send message to Video-Processing service to transcode video
        } catch (Exception e) {
            throw new FileUploadException("Exception while reading uploaded file", e);
        }
    }

    private RawVideo createRawVideo(final MultipartFile multipartFile, final Videos video, final String fileName) {
        final RawVideo rawVideo = new RawVideo();
        rawVideo.setVideoId(video.getId());
        rawVideo.setLink(fileName);
        rawVideo.setSize(multipartFile.getSize());
        return rawVideo;
    }

    private Videos createVideo(final String userId, final MultipartFile multipartFile, final UploadVideoRequest videoRequest) {
        final Videos videos = new Videos();
        videos.setTitle(videoRequest.getTitle());
        videos.setDescription(videoRequest.getDescription());
        videos.setStatus(TranscodingStatus.CREATED);
        videos.setUserId(UUID.fromString(userId));
        videos.setLength(multipartFile.getSize());
        return videos;
    }

    private String generateFileName(final String name) {
        return LocalDateTime.now() + "-" + StringUtils.replace(name, " ", "_");
    }
}

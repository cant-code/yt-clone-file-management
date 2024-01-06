package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;
import com.cantcode.yt.filemanagement.webapp.model.UploadVideoRequest;
import com.cantcode.yt.filemanagement.webapp.repository.VideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
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

    public FileServiceImpl(final S3Client s3Client, final VideosRepository videosRepository) {
        this.s3Client = s3Client;
        this.videosRepository = videosRepository;
    }

    @Override
    public void uploadFile(final String userId, final MultipartFile multipartFile, final UploadVideoRequest videoRequest) {
        try {
            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket("test")
                    .key(generateFileName(multipartFile.getName()))
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(multipartFile.getBytes()));

            final Videos video = createVideo(userId, multipartFile, videoRequest);

            videosRepository.save(video);
        } catch (Exception e) {
            throw new RuntimeException("Exception while reading uploaded file", e);
        }
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

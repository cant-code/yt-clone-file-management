package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.configs.properties.S3BucketProperties;
import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;
import com.cantcode.yt.filemanagement.webapp.exceptions.FileUploadException;
import com.cantcode.yt.filemanagement.webapp.model.FileManagementMessage;
import com.cantcode.yt.filemanagement.webapp.model.FileProcessingMessage;
import com.cantcode.yt.filemanagement.webapp.model.UploadVideoRequest;
import com.cantcode.yt.filemanagement.webapp.repository.EncodedVideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.RawVideoRepository;
import com.cantcode.yt.filemanagement.webapp.repository.VideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.entities.EncodedVideo;
import com.cantcode.yt.filemanagement.webapp.repository.entities.RawVideo;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import com.cantcode.yt.filemanagement.webapp.service.messaging.MessagingService;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final S3Client s3Client;
    private final MessagingService messagingService;
    private final VideosRepository videosRepository;
    private final RawVideoRepository rawVideoRepository;
    private final S3BucketProperties s3BucketProperties;
    private final EncodedVideosRepository encodedVideosRepository;

    public FileServiceImpl(final S3Client s3Client,
                           final VideosRepository videosRepository,
                           final RawVideoRepository rawVideoRepository,
                           final S3BucketProperties s3BucketProperties,
                           final MessagingService messagingService,
                           final EncodedVideosRepository encodedVideosRepository) {
        this.s3Client = s3Client;
        this.videosRepository = videosRepository;
        this.rawVideoRepository = rawVideoRepository;
        this.s3BucketProperties = s3BucketProperties;
        this.messagingService = messagingService;
        this.encodedVideosRepository = encodedVideosRepository;
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

            log.info("Uploading file to S3 for user: {} with fileName: {}", userId, fileName);
            s3Client.putObject(request, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            final Videos video = videosRepository.save(createVideo(userId, multipartFile, videoRequest));

            final RawVideo rawVideo = rawVideoRepository.save(createRawVideo(multipartFile, video, fileName));

            messagingService.sendMessage(getFileProcessingMessage(rawVideo));
        } catch (Exception e) {
            log.error("Error while uploading file for userId: {}", userId);
            throw new FileUploadException("Error while uploading file", e);
        }
    }

    @Override
    @Transactional
    public void processMessage(final FileManagementMessage message) {
        saveEncodedVideos(message);
        updateVideoStatus(message.getFileId());
    }

    private void saveEncodedVideos(final FileManagementMessage message) {
        if (!isEmpty(message.getFiles())) {
            final List<EncodedVideo> videos = new ArrayList<>();
            message.getFiles().forEach(fileDetail -> {
                final EncodedVideo video = new EncodedVideo();
                video.setVideoId(message.getFileId());
                video.setLink(fileDetail.getFileName());
                video.setQuality(String.valueOf(fileDetail.getQuality()));
                videos.add(video);
            });
            encodedVideosRepository.saveAll(videos);
        }
    }

    private void updateVideoStatus(final Long videoId) {
        videosRepository.findById(videoId).ifPresent(videos -> videos.setStatus(TranscodingStatus.PROCESSED));
    }

    private FileProcessingMessage getFileProcessingMessage(final RawVideo rawVideo) {
        final FileProcessingMessage message = new FileProcessingMessage();
        message.setFileId(rawVideo.getVideoId());
        message.setFileName(rawVideo.getLink());
        return message;
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

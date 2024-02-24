package com.cantcode.yt.filemanagement.webapp.service.impl;

import com.cantcode.yt.filemanagement.webapp.configs.properties.S3BucketProperties;
import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;
import com.cantcode.yt.filemanagement.webapp.exceptions.FileUploadException;
import com.cantcode.yt.filemanagement.webapp.model.*;
import com.cantcode.yt.filemanagement.webapp.repository.EncodedVideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.RawVideoRepository;
import com.cantcode.yt.filemanagement.webapp.repository.VideosRepository;
import com.cantcode.yt.filemanagement.webapp.repository.entities.EncodedVideo;
import com.cantcode.yt.filemanagement.webapp.repository.entities.RawVideo;
import com.cantcode.yt.filemanagement.webapp.repository.entities.Videos;
import com.cantcode.yt.filemanagement.webapp.service.messaging.MessagingService;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
import com.cantcode.yt.filemanagement.webapp.service.spi.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
    private final S3Service s3Service;
    private final MessagingService messagingService;
    private final VideosRepository videosRepository;
    private final RawVideoRepository rawVideoRepository;
    private final S3BucketProperties s3BucketProperties;
    private final EncodedVideosRepository encodedVideosRepository;

    public FileServiceImpl(final S3Service s3Service,
                           final VideosRepository videosRepository,
                           final RawVideoRepository rawVideoRepository,
                           final S3BucketProperties s3BucketProperties,
                           final MessagingService messagingService,
                           final EncodedVideosRepository encodedVideosRepository) {
        this.s3Service = s3Service;
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

            String logFileName = fileName.replaceAll("[\n\r]", "_");
            log.info("Uploading file to S3 for user: {} with fileName: {}", userId, logFileName);
            s3Service.putObject(request, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            final Videos video = videosRepository.save(createVideo(userId, videoRequest));

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
        updateVideoStatus(message);
    }

    @Override
    @Transactional(readOnly = true)
    public StreamBodyResponse downloadVideo(final Long videoId, final String quality) {
        final EncodedVideo encodedVideo = encodedVideosRepository.findByVideoIdAndQuality(videoId, quality).orElseThrow();
        final GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3BucketProperties.getTranscodedVideos())
                .key(encodedVideo.getLink())
                .build();
        return new StreamBodyResponse(s3Service.getObject(request)::transferTo, encodedVideo.getSize());
    }

    private void saveEncodedVideos(final FileManagementMessage message) {
        if (!isEmpty(message.getFiles())) {
            final List<EncodedVideo> videos = new ArrayList<>();
            message.getFiles()
                    .forEach(fileDetail -> {
                        if (fileDetail.isSuccess()) {
                            final EncodedVideo video = new EncodedVideo();
                            video.setVideoId(message.getFileId());
                            video.setLink(fileDetail.getFileName());
                            video.setQuality(String.valueOf(fileDetail.getQuality()));
                            video.setSize(fileDetail.getSize());
                            videos.add(video);
                        } else {
                            log.error(fileDetail.getError());
                        }
                    });
            encodedVideosRepository.saveAll(videos);
        }
        log.info("Saved Encoded videos for videoId: {}", message.getFileId());
    }

    private void updateVideoStatus(final FileManagementMessage message) {
        final long count = message.getFiles().stream().filter(FileDetail::isSuccess).count();
        final TranscodingStatus status = switch ((int) count) {
            case 3:
                yield TranscodingStatus.PROCESSED;
            case 0:
                yield TranscodingStatus.FAILED;
            default:
                yield TranscodingStatus.PARTIALLY_PROCESSED;
        };
        videosRepository.findById(message.getFileId()).ifPresent(videos -> videos.setStatus(status));
        log.info("Updated video status for videoId: {}", message.getFileId());
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

    private Videos createVideo(final String userId, final UploadVideoRequest videoRequest) {
        final Videos videos = new Videos();
        videos.setTitle(videoRequest.getTitle());
        videos.setDescription(videoRequest.getDescription());
        videos.setStatus(TranscodingStatus.CREATED);
        videos.setUserId(UUID.fromString(userId));
        return videos;
    }

    private String generateFileName(final String name) {
        return LocalDateTime.now() + "-" + StringUtils.replace(name, " ", "_");
    }
}

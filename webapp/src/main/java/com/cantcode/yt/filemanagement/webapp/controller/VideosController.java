package com.cantcode.yt.filemanagement.webapp.controller;

import com.cantcode.yt.filemanagement.webapp.model.PageModel;
import com.cantcode.yt.filemanagement.webapp.model.StreamBodyResponse;
import com.cantcode.yt.filemanagement.webapp.model.VideoListResponse;
import com.cantcode.yt.filemanagement.webapp.service.spi.StreamingService;
import com.cantcode.yt.filemanagement.webapp.service.spi.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.*;
import static com.cantcode.yt.filemanagement.webapp.controller.Range.parseHttpRangeString;
import static com.cantcode.yt.filemanagement.webapp.utils.StreamingConstants.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

@RestController
@RequestMapping(path = VIDEOS_BASE_URL)
public class VideosController {

    private static final Logger log = LoggerFactory.getLogger(VideosController.class);
    private final StreamingService streamingService;
    private final VideoService videoService;

    public VideosController(final StreamingService streamingService, VideoService videoService) {
        this.streamingService = streamingService;
        this.videoService = videoService;
    }

    @GetMapping(path = STREAM_VIDEO)
    public ResponseEntity<StreamingResponseBody> streamVideo(@PathVariable(name = "id") final Long fileId,
                                                             @RequestHeader(value = RANGE, required = false) final String range) {
        log.info("Streaming video for fileId: {}", fileId);
        final Range parsedRange = parseHttpRangeString(range, CHUNK_SIZE);
        final StreamBodyResponse streamBody = streamingService.streamVideo(fileId, range);
        return ResponseEntity.status(PARTIAL_CONTENT)
                .contentType(MediaType.valueOf(MP4_MEDIA_TYPE))
                .header(ACCEPT_RANGES, ACCEPTED_RANGE_BYTES)
                .header(CONTENT_RANGE, constructContentRangeHeader(parsedRange, streamBody.length()))
                .header(CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, streamBody.length()))
                .body(streamBody.body());
    }

    @GetMapping
    public ResponseEntity<VideoListResponse> getVideosList(final PageModel pageModel) {
        log.info("Fetching videos page");
        return ResponseEntity.ok(videoService.getVideoPage(pageModel));
    }
}

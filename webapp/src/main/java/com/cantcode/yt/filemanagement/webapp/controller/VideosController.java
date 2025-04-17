package com.cantcode.yt.filemanagement.webapp.controller;

import com.cantcode.yt.filemanagement.webapp.model.*;
import com.cantcode.yt.filemanagement.webapp.service.spi.StreamingService;
import com.cantcode.yt.filemanagement.webapp.service.spi.VideoService;
import com.cantcode.yt.filemanagement.webapp.utils.Range;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.*;
import static com.cantcode.yt.filemanagement.webapp.utils.Range.parseHttpRangeString;
import static com.cantcode.yt.filemanagement.webapp.utils.StreamingConstants.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

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

    @Operation(summary = "Stream Video in chunks", responses = {
            @ApiResponse(responseCode = "206", description = "Video chunk", content = @Content(mediaType = "video/*",
                    schema = @Schema(implementation = StreamingResponseBody.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Param", content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = GenericExceptionBody.class)))
    })
    @GetMapping(path = STREAM_VIDEO)
    public ResponseEntity<StreamingResponseBody> streamVideo(@RequestHeader(value = RANGE, required = false) final String range,
                                                             @PathVariable(name = "id") final Long fileId,
                                                             @RequestParam(name = "quality", defaultValue = "360") final String quality) {
        log.info("Streaming video for fileId: {}", fileId);
        final Range parsedRange = parseHttpRangeString(range, CHUNK_SIZE);
        final StreamBodyResponse streamBody = streamingService.streamVideo(fileId, quality, range);
        return ResponseEntity.status(PARTIAL_CONTENT)
                .contentType(MediaType.valueOf(MP4_MEDIA_TYPE))
                .header(ACCEPT_RANGES, ACCEPTED_RANGE_BYTES)
                .header(CONTENT_RANGE, constructContentRangeHeader(parsedRange, streamBody.length()))
                .header(CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, streamBody.length()))
                .body(streamBody.body());
    }

    @Operation(summary = "Get Video page", responses = {
            @ApiResponse(responseCode = "200", description = "Video Page", content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = VideoListResponse.class)))
    })
    @GetMapping
    public ResponseEntity<VideoListResponse> getVideosList(@ParameterObject final PageModel pageModel) {
        log.info("Fetching videos page");
        return ResponseEntity.ok(videoService.getVideoPage(pageModel));
    }

    @Operation(summary = "Get Video page for logged in user", responses = {
            @ApiResponse(responseCode = "200", description = "Video Page", content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = VideoListResponse.class)))
    })
    @GetMapping(STATUS)
    public ResponseEntity<VideoListResponse> getVideosPageForUser(final JwtAuthenticationToken principal,
                                                                  @ParameterObject final PageModel pageModel) {
        log.info("Fetching videos page for user: {}", principal.getToken().getSubject());
        return ResponseEntity.ok(videoService.getVideoPageForUser(principal.getToken().getSubject(), pageModel));
    }

    @Operation(summary = "Get Video by video-id", responses = {
            @ApiResponse(responseCode = "200", description = "Video", content = @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = VideoListResponse.class)))
    })
    @GetMapping(VIDEO_ID)
    public ResponseEntity<VideoDetail> getVideoById(@PathVariable(name = "id") final Long videoId) {
        return ResponseEntity.ok(videoService.getVideoById(videoId));
    }
}

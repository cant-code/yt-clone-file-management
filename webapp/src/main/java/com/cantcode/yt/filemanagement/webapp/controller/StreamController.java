package com.cantcode.yt.filemanagement.webapp.controller;

import com.cantcode.yt.filemanagement.webapp.model.StreamBodyResponse;
import com.cantcode.yt.filemanagement.webapp.service.spi.StreamingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.FILE_MANAGEMENT_BASE_URL;
import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.STREAM_VIDEO;
import static com.cantcode.yt.filemanagement.webapp.controller.Range.parseHttpRangeString;
import static com.cantcode.yt.filemanagement.webapp.utils.StreamingConstants.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

@RestController
@RequestMapping(path = FILE_MANAGEMENT_BASE_URL)
public class StreamController {

    private static final Logger log = LoggerFactory.getLogger(StreamController.class);
    private final StreamingService streamingService;

    public StreamController(final StreamingService streamingService) {
        this.streamingService = streamingService;
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
}

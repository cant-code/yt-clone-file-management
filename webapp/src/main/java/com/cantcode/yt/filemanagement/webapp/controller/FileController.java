package com.cantcode.yt.filemanagement.webapp.controller;

import com.cantcode.yt.filemanagement.webapp.model.StreamBodyResponse;
import com.cantcode.yt.filemanagement.webapp.model.UploadVideoRequest;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.time.LocalDate;

import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.*;
import static com.cantcode.yt.filemanagement.webapp.utils.StreamingConstants.MP4_MEDIA_TYPE;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(path = VIDEOS_BASE_URL)
@SecurityRequirement(name = "oauth2")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;

    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Upload video", responses = {
            @ApiResponse(responseCode = "200", description = "Video uploaded successfully")
    })
    @PostMapping(path = UPLOAD, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadVideo(final JwtAuthenticationToken principal,
                                            @RequestPart final MultipartFile file,
                                            @RequestPart(name = "videoDetails") final UploadVideoRequest request) {
        log.info("Uploading file for user: {}", principal.getToken().getSubject());
        fileService.uploadFile(principal.getToken().getSubject(), file, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Download video", responses = {
            @ApiResponse(responseCode = "200", description = "Download video",
                    content = @Content(schema = @Schema(contentSchema = StreamingResponseBody.class)))
    })
    @GetMapping(path = DOWNLOAD_VIDEO)
    public ResponseEntity<StreamingResponseBody> downloadVideo(@PathVariable("id") final Long videoId,
                                                               @RequestParam(defaultValue = "360") final String quality) {
        log.info("Download file for videoId: {}", videoId);
        final StreamBodyResponse streamBodyResponse = fileService.downloadVideo(videoId, quality);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(MP4_MEDIA_TYPE))
                .header(CONTENT_LENGTH, String.valueOf(streamBodyResponse.length()))
                .header(CONTENT_DISPOSITION, "attachment; filename=%s_%d_%s.mp4"
                        .formatted(LocalDate.now(), videoId, quality))
                .body(streamBodyResponse.body());
    }
}

package com.cantcode.yt.filemanagement.webapp.controller;

import com.cantcode.yt.filemanagement.webapp.model.UploadVideoRequest;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.FILE_MANAGEMENT_BASE_URL;
import static com.cantcode.yt.filemanagement.webapp.controller.APIDefinition.VIDEO;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(path = FILE_MANAGEMENT_BASE_URL)
public class FileController {

    private final FileService fileService;

    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = VIDEO, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadVideo(final JwtAuthenticationToken principal,
                                            @RequestPart final MultipartFile file,
                                            @RequestPart(name = "videoDetails") final UploadVideoRequest request) {
        fileService.uploadFile(principal.getToken().getSubject(), file, request);
        return ResponseEntity.ok(null);
    }
}

package com.cantcode.yt.filemanagement.webapp.controller;

import com.cantcode.yt.filemanagement.webapp.exceptions.FileUploadException;
import com.cantcode.yt.filemanagement.webapp.model.GenericExceptionBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<GenericExceptionBody> handleFileUploadException(final FileUploadException e) {
        log.error(e.getMessage(), e);

        return ResponseEntity
                .badRequest()
                .body(createGenericResponseBody(e));
    }

    private GenericExceptionBody createGenericResponseBody(final Exception e) {
        GenericExceptionBody body = new GenericExceptionBody();
        body.setMessage(e.getMessage());
        return body;
    }
}

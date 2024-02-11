package com.cantcode.yt.filemanagement.webapp.exceptions;

public class FileUploadException extends GeneralBadRequestException {

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

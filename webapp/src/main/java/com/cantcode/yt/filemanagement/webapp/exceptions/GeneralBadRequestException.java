package com.cantcode.yt.filemanagement.webapp.exceptions;

public class GeneralBadRequestException extends RuntimeException {

    public GeneralBadRequestException(String message) {
        super(message);
    }

    public GeneralBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

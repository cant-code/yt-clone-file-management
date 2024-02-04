package com.cantcode.yt.filemanagement.webapp.model;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public record StreamBodyResponse(
        StreamingResponseBody body,
        Long length
) {
}

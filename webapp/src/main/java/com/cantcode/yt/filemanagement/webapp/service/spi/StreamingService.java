package com.cantcode.yt.filemanagement.webapp.service.spi;

import com.cantcode.yt.filemanagement.webapp.model.StreamBodyResponse;

public interface StreamingService {

    StreamBodyResponse streamVideo(Long fileId, String range);
}

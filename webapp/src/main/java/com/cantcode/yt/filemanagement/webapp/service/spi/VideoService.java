package com.cantcode.yt.filemanagement.webapp.service.spi;

import com.cantcode.yt.filemanagement.webapp.model.PageModel;
import com.cantcode.yt.filemanagement.webapp.model.VideoListResponse;

public interface VideoService {

    VideoListResponse getVideoPage(PageModel pageModel);

    VideoListResponse getVideoPageForUser(String userId, PageModel pageModel);
}

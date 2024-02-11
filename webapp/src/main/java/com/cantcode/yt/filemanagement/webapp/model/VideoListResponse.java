package com.cantcode.yt.filemanagement.webapp.model;

import java.util.List;

public class VideoListResponse {

    private List<VideoDetail> videoDetails;
    private PageModel page;

    public List<VideoDetail> getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(List<VideoDetail> videoDetails) {
        this.videoDetails = videoDetails;
    }

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }
}

package com.cantcode.yt.filemanagement.webapp.controller;

public class APIDefinition {

    public static final String FILE_MANAGEMENT_BASE_URL = "/api/file-management";
    public static final String VIDEO = "/videos";
    public static final String VIDEOS_BASE_URL = FILE_MANAGEMENT_BASE_URL + VIDEO;
    public static final String UPLOAD = "/upload";
    public static final String STREAM_VIDEO = "/{id}/stream";
    public static final String DOWNLOAD_VIDEO = "/{id}/download";
    public static final String STATUS = "/status";
    public static final String VIDEO_ID = "/{id}";

    private APIDefinition() {}
}

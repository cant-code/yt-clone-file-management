package com.cantcode.yt.filemanagement.webapp.model;

import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;

import java.util.UUID;

public class VideoDetail {

    private Long id;

    private UUID userId;

    private String title;

    private String description;

    private Long length;

    private TranscodingStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public TranscodingStatus getStatus() {
        return status;
    }

    public void setStatus(TranscodingStatus status) {
        this.status = status;
    }
}

package com.cantcode.yt.filemanagement.webapp.model;

import java.util.List;

public class FileManagementMessage {
    private Long fileId;
    private List<FileDetail> files;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public List<FileDetail> getFiles() {
        return files;
    }

    public void setFiles(List<FileDetail> files) {
        this.files = files;
    }
}

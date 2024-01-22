package com.cantcode.yt.filemanagement.webapp.model;

import java.util.List;

public class FileManagementMessage {
    private String fileId;
    private List<FileDetail> files;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<FileDetail> getFiles() {
        return files;
    }

    public void setFiles(List<FileDetail> files) {
        this.files = files;
    }
}

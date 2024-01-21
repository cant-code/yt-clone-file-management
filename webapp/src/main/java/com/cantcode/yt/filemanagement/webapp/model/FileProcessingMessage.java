package com.cantcode.yt.filemanagement.webapp.model;

public class FileProcessingMessage {
    private Long fileId;
    private String fileName;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

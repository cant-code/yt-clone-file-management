package com.cantcode.yt.filemanagement.webapp.service.spi;

import com.cantcode.yt.filemanagement.webapp.model.FileManagementMessage;
import com.cantcode.yt.filemanagement.webapp.model.UploadVideoRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void uploadFile(String userId, MultipartFile multipartFile, UploadVideoRequest request);

    void processMessage(FileManagementMessage message);
}

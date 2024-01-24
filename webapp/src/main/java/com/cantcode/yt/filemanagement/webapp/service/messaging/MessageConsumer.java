package com.cantcode.yt.filemanagement.webapp.service.messaging;

import com.cantcode.yt.filemanagement.webapp.model.FileManagementMessage;
import com.cantcode.yt.filemanagement.webapp.service.spi.FileService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private final FileService fileService;

    public MessageConsumer(FileService fileService) {
        this.fileService = fileService;
    }

    @JmsListener(destination = "${spring.artemis.managementQueue}")
    public void receiveMessage(final FileManagementMessage message) {
        fileService.processMessage(message);
    }
}

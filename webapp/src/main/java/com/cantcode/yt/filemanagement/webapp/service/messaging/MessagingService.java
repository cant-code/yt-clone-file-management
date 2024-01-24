package com.cantcode.yt.filemanagement.webapp.service.messaging;

import com.cantcode.yt.filemanagement.webapp.model.FileProcessingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
    private final JmsTemplate jmsTemplate;
    private final String transcodingQueue;

    public MessagingService(final JmsTemplate jmsTemplate,
                            @Value("${spring.artemis.transcodingQueue}") final String transcodingQueue) {
        this.jmsTemplate = jmsTemplate;
        this.transcodingQueue = transcodingQueue;
    }

    public void sendMessage(final FileProcessingMessage message) {
        try {
            jmsTemplate.convertAndSend(transcodingQueue, message);
        } catch (JmsException e) {
            log.error("Error sending message", e);
        }
    }
}

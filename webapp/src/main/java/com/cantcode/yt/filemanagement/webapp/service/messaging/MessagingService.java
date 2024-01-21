package com.cantcode.yt.filemanagement.webapp.service.messaging;

import com.cantcode.yt.filemanagement.webapp.model.FileProcessingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessagingService {

    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
    private final JmsTemplate jmsTemplate;
    private final String queue;

    public MessagingService(final JmsTemplate jmsTemplate, @Value("${spring.artemis.queue}") final String queue) {
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }

    public void sendMessage(final FileProcessingMessage message) {
        try {
            jmsTemplate.convertAndSend(queue, message);
        } catch (JmsException e) {
            log.error("Error sending message", e);
        }
    }
}

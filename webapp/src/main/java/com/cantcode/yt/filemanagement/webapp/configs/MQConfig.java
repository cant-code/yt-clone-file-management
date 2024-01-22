package com.cantcode.yt.filemanagement.webapp.configs;

import com.cantcode.yt.filemanagement.webapp.model.FileManagementMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.*;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MQConfig implements MessageConverter {

    private final ObjectMapper mapper;

    public MQConfig(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Message toMessage(final Object object, final Session session) throws JMSException, MessageConversionException {
        String json;
        try {
            json = mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new MessageConversionException("Message cannot be parsed. ", e);
        }
        TextMessage message = session.createTextMessage();
        message.setText(json);

        return message;
    }

    @Override
    public Object fromMessage(final Message message) throws JMSException, MessageConversionException {
        try {
            if (message instanceof TextMessage textmessage) {
                return mapper.readValue(textmessage.getText(), FileManagementMessage.class);
            } else if (message instanceof BytesMessage bytesMessage) {
                return mapper.readValue(bytesMessage.getBody(byte[].class), FileManagementMessage.class);
            }
        } catch (IOException e) {
            throw new MessageConversionException("Message cannot be deserialized");
        }
        throw new MessageConversionException("Message cannot be deserialized");
    }
}

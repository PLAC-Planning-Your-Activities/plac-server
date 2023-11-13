package com.plac.util;

import com.plac.domain.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageUtil {

    public static ResponseEntity<Message> buildResponseEntity(HttpStatus status, String msg) {
        Message message = Message.builder()
                .status(status)
                .message(msg)
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }

    public static ResponseEntity<Message> buildResponseEntity(Object data, HttpStatus status, String msg) {
        Message message = Message.builder()
                .data(data)
                .status(status)
                .message(msg)
                .build();
        return new ResponseEntity<>(message, message.getStatus());
    }

    public static Message buildMessage(HttpStatus status, String msg) {
        return Message.builder()
                .status(status)
                .message(msg)
                .build();
    }

    public static Message buildMessage(Object data, HttpStatus status, String msg) {
        return Message.builder()
                .status(status)
                .message(msg)
                .build();
    }

}

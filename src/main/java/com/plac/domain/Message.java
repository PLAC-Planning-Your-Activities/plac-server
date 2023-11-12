package com.plac.domain;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Object data;
    private HttpStatus status;
    private int code;
    private String message;

    public Message(String message, int code, HttpStatus status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
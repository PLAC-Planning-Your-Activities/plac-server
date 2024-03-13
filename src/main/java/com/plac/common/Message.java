package com.plac.common;

import lombok.*;

@Getter
@Builder
public class Message {
    private Object data;

    public Message(Object data) {
        this.data = data;
    }
}
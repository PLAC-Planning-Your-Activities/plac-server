package com.plac.exception;

import lombok.Getter;

@Getter
public class BaseErrorResponse {

    private String error;

    public BaseErrorResponse(String error) {
        this.error = error;
    }
}

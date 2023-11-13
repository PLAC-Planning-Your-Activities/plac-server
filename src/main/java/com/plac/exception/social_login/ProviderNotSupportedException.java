package com.plac.exception.social_login;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProviderNotSupportedException extends RuntimeException {
    private String message;

    public ProviderNotSupportedException(String message) {
        super(message);
        this.message = message;
    }
}

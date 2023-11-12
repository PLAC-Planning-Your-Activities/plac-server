package com.plac.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WeakPasswordException extends RuntimeException{

    private String message;

    public WeakPasswordException(String message) {
        super(message);
        this.message = message;
    }
}

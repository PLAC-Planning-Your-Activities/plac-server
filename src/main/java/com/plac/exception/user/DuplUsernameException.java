package com.plac.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplUsernameException extends RuntimeException{

    private String message;

    public DuplUsernameException(String message) {
        super(message);
        this.message = message;
    }
}

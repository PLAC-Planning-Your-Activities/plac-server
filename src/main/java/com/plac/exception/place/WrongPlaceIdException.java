package com.plac.exception.place;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongPlaceIdException extends RuntimeException {

    private String message;
    public WrongPlaceIdException(String message) {
        super(message);
        this.message = message;
    }
}

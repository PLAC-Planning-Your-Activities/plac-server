package com.plac.exception.plan;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FavoritePlanException extends RuntimeException {

    private String message;

    public FavoritePlanException(String message) {
        super(message);
        this.message = message;
    }
}

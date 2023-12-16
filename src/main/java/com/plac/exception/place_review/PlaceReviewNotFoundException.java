package com.plac.exception.place_review;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlaceReviewNotFoundException extends RuntimeException {
    private String message;

    public PlaceReviewNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}

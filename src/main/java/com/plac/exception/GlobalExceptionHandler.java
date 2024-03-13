package com.plac.exception;

import com.plac.exception.common.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * developer custom exception
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrorResponse> handleBaseException(BaseException exception) {
        return ResponseEntity
                .status(exception.getCode())
                .body(new BaseErrorResponse(exception.getMessage()));
    }
}

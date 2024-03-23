package com.plac.exception;

import com.plac.exception.common.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseErrorResponse> handleRequestValidException(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMessage = String.join(" ", errors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseErrorResponse(errorMessage));
    }
}

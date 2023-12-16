package com.plac.exception;

import com.plac.domain.Message;
import com.plac.exception.place.WrongKakaoPlaceIdException;
import com.plac.exception.place.WrongPlaceIdException;
import com.plac.exception.place_review.CannotRateReviewException;
import com.plac.exception.place_review.PlaceReviewNotFoundException;
import com.plac.exception.social_login.ProviderNotSupportedException;
import com.plac.exception.user.DuplUsernameException;
import com.plac.exception.user.UserPrincipalNotFoundException;
import com.plac.exception.user.WeakPasswordException;
import com.plac.exception.user.WrongLoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DuplUsernameException.class)
    public ResponseEntity<?> handleException(DuplUsernameException e){
        Message message = new Message(e.getMessage(), -100, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleException(UsernameNotFoundException e){
        Message message = new Message(e.getMessage(), -101, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseEntity<?> handleException(UserPrincipalNotFoundException e){
        Message message = new Message(e.getMessage(), -102, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<?> handleException(WeakPasswordException e){
        Message message = new Message(e.getMessage(), -103, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(WrongLoginException.class)
    public ResponseEntity<?> handleException(WrongLoginException e){
        Message message = new Message(e.getMessage(), -104, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(ProviderNotSupportedException.class)
    public ResponseEntity<?> handleException(ProviderNotSupportedException e){
        Message message = new Message(e.getMessage(), -201, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(WrongKakaoPlaceIdException.class)
    public ResponseEntity<?> handleException(WrongKakaoPlaceIdException e){
        Message message = new Message(e.getMessage(), -300, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(WrongPlaceIdException.class)
    public ResponseEntity<?> handleException(WrongPlaceIdException e){
        Message message = new Message(e.getMessage(), -301, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(PlaceReviewNotFoundException.class)
    public ResponseEntity<?> handleException(PlaceReviewNotFoundException e){
        Message message = new Message(e.getMessage(), -302, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

    @ExceptionHandler(CannotRateReviewException.class)
    public ResponseEntity<?> handleException(CannotRateReviewException e){
        Message message = new Message(e.getMessage(), -303, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, message.getStatus());
    }

}

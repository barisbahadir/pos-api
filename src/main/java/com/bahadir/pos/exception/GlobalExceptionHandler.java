package com.bahadir.pos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<?> handleJwtTokenException(JwtTokenException ex) {
        return new ResponseEntity<>(new ErrorResponse("(JWT) " + ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleJwtTokenException(ApiException ex) {
        return new ResponseEntity<>(new ErrorResponse("(API) " + ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("(EXC) " + ex.getMessage()), HttpStatus.FORBIDDEN);
    }
}
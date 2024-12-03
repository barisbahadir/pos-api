package com.bahadir.pos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<?> handleJwtTokenException(JwtTokenException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Internal Server Error: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
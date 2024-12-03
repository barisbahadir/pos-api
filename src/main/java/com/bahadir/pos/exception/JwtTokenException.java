package com.bahadir.pos.exception;

public class JwtTokenException extends RuntimeException {
    public JwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenException(String message) {
        super(message);
    }
}
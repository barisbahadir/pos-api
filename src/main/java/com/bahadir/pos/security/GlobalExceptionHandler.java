package com.bahadir.pos.security;

import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.exception.JwtTokenException;
import com.bahadir.pos.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static HttpStatus defaultApiStatus = HttpStatus.FORBIDDEN;
    private static HttpStatus authApiStatus = HttpStatus.UNAUTHORIZED;

    // JWT Token hatası için özel handler
    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtTokenException(JwtTokenException exc) {
        ApiResponse<Void> response = ApiResponse.error(authApiStatus.value(), exc, "JWT");
        return new ResponseEntity<>(response, authApiStatus);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exc) {
        ApiResponse<Void> response = ApiResponse.error(authApiStatus.value(), exc, "AUTH");
        return new ResponseEntity<>(response, authApiStatus);
    }

    // ApiException hatası için özel handler
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException exc) {
        ApiResponse<Void> response = ApiResponse.error(defaultApiStatus.value(), exc, "API");
        return new ResponseEntity<>(response, defaultApiStatus);
    }

    // AccessDeniedException (403) hatası için özel handler
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exc) {
        ApiResponse<Void> response = ApiResponse.error(defaultApiStatus.value(), exc, "ACCESS_DENIED");
        return new ResponseEntity<>(response, defaultApiStatus);
    }

    // Genel Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception exc) {
        ApiResponse<Void> response = ApiResponse.error(defaultApiStatus.value(), exc, "EXCEPTION");
        return new ResponseEntity<>(response, defaultApiStatus);
    }

    // Diğer tüm hatalar için genel handler
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<?>> handleThrowable(Throwable exc) {
        ApiResponse<Void> response = ApiResponse.error(defaultApiStatus.value(), exc, "THROWABLE");
        return new ResponseEntity<>(response, defaultApiStatus);
    }
}
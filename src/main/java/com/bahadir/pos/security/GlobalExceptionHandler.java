package com.bahadir.pos.security;

import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.exception.JwtTokenException;
import com.bahadir.pos.utils.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static HttpStatus defaultApiStatus = HttpStatus.FORBIDDEN;
    private static HttpStatus authApiStatus = HttpStatus.UNAUTHORIZED;

    // JWT Token hatası için özel handler
    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtTokenException(JwtTokenException exc) {
        return getApiResponse(authApiStatus, exc, "JWT");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exc) {
        return getApiResponse(authApiStatus, exc, "AUTH");
    }

    // ApiException hatası için özel handler
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException exc) {
        return getApiResponse(defaultApiStatus, exc, "API");
    }

    // AccessDeniedException (403) hatası için özel handler
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exc) {
        return getApiResponse(defaultApiStatus, exc, "ACCESS_DENIED");
    }

    // Validation Errors - @Valid annotation hataları için
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException exc) {
        StringBuilder errorMessage = new StringBuilder();
        exc.getBindingResult().getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(", "));

        return getApiResponse(defaultApiStatus, new Throwable(errorMessage.toString()), "VALIDATION");
    }

    // ConstraintViolationException hatası için handler
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException exc) {
        StringBuilder errorMessage = new StringBuilder();
        exc.getConstraintViolations().forEach(violation -> errorMessage.append(violation.getMessage()).append(", "));

        return getApiResponse(defaultApiStatus, new Throwable(errorMessage.toString()), "CONSTRAINT_VIOLATION");
    }

    // NoHandlerFoundException hatası için handler
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException exc) {
        return getApiResponse(defaultApiStatus, exc, "NO_HANDLER_FOUND");
    }

    // HttpRequestMethodNotSupportedException hatası için handler
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exc) {
        return getApiResponse(defaultApiStatus, exc, "METHOD_NOT_ALLOWED");
    }

    // DataAccessException hatası için handler
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleDataAccessException(DataAccessException exc) {
        return getApiResponse(defaultApiStatus, exc, "DATABASE_ERROR");
    }

    // ResourceNotFoundException hatası için handler
//@ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exc) {
//        ApiResponse<Void> response = ApiResponse.error(defaultApiStatus.value(), exc, "RESOURCE_NOT_FOUND");
//        return new ResponseEntity<>(response, defaultApiStatus);
//    }

    // TimeoutException hatası için handler
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ApiResponse<?>> handleTimeoutException(TimeoutException exc) {
        return getApiResponse(defaultApiStatus, exc, "TIMEOUT");
    }

    // IOException hatası için handler
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<?>> handleIOException(IOException exc) {
        return getApiResponse(defaultApiStatus, exc, "IO_ERROR");
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc) {
        String errorMessage = String.format("Parameter '%s' should be of type '%s'", exc.getName(), exc.getRequiredType().getName());

        return getApiResponse(defaultApiStatus, new Throwable(errorMessage), "ARGUMENT_TYPE_MISMATCH");
    }

    // Genel Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception exc) {
        return getApiResponse(defaultApiStatus, exc, "EXCEPTION");
    }

    // Diğer tüm hatalar için genel handler
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<?>> handleThrowable(Throwable exc) {
        return getApiResponse(defaultApiStatus, exc, "THROWABLE");
    }

    private ResponseEntity<ApiResponse<?>> getApiResponse(HttpStatus status, Throwable exc, String source) {
        ApiResponse<Void> response = ApiResponse.error(status.value(), exc, source);
        return new ResponseEntity<>(response, status);
    }
}
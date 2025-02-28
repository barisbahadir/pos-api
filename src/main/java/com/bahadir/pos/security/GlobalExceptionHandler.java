package com.bahadir.pos.security;

import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.exception.JwtTokenException;
import com.bahadir.pos.service.SystemLogService;
import com.bahadir.pos.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
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

    private final SystemLogService systemLogService;

    public GlobalExceptionHandler(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    // JWT Token hatası için özel handler
    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleJwtTokenException(JwtTokenException exc, HttpServletRequest request) {
        return getApiResponse(authApiStatus, exc, "JWT", request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthenticationException(AuthenticationException exc, HttpServletRequest request) {
        return getApiResponse(authApiStatus, exc, "AUTH", request);
    }

    // ApiException hatası için özel handler
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "API", request);
    }

    // AccessDeniedException (403) hatası için özel handler
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "ACCESS_DENIED", request);
    }

    // Validation Errors - @Valid annotation hataları için
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException exc, HttpServletRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        exc.getBindingResult().getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append(", "));

        return getApiResponse(defaultApiStatus, new Throwable(errorMessage.toString()), "VALIDATION", request);
    }

    // ConstraintViolationException hatası için handler
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException exc, HttpServletRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        exc.getConstraintViolations().forEach(violation -> errorMessage.append(violation.getMessage()).append(", "));

        return getApiResponse(defaultApiStatus, new Throwable(errorMessage.toString()), "CONSTRAINT_VIOLATION", request);
    }

    // NoHandlerFoundException hatası için handler
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "NO_HANDLER_FOUND", request);
    }

    // HttpRequestMethodNotSupportedException hatası için handler
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "METHOD_NOT_ALLOWED", request);
    }

    // DataAccessException hatası için handler
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleDataAccessException(DataAccessException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "DATABASE_ERROR", request);
    }

    // ResourceNotFoundException hatası için handler
//@ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exc) {
//        ApiResponse<Void> response = ApiResponse.error(defaultApiStatus.value(), exc, "RESOURCE_NOT_FOUND");
//        return new ResponseEntity<>(response, defaultApiStatus);
//    }

    // TimeoutException hatası için handler
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ApiResponse<?>> handleTimeoutException(TimeoutException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "TIMEOUT", request);
    }

    // IOException hatası için handler
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<?>> handleIOException(IOException exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "IO_ERROR", request);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exc, HttpServletRequest request) {
        String errorMessage = String.format("Parameter '%s' should be of type '%s'", exc.getName(), exc.getRequiredType().getName());

        return getApiResponse(defaultApiStatus, new Throwable(errorMessage), "ARGUMENT_TYPE_MISMATCH", request);
    }

    // Genel Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "EXCEPTION", request);
    }

    // Diğer tüm hatalar için genel handler
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiResponse<?>> handleThrowable(Throwable exc, HttpServletRequest request) {
        return getApiResponse(defaultApiStatus, exc, "THROWABLE", request);
    }

    private ResponseEntity<ApiResponse<?>> getApiResponse(HttpStatus status, Throwable exc, String source, HttpServletRequest request) {
        ApiResponse<Void> response = ApiResponse.error(status.value(), exc, source);

        systemLogService.saveLog(request, exc, status, source);

        return new ResponseEntity<>(response, status);
    }
}
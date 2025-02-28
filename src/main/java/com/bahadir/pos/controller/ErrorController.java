package com.bahadir.pos.controller;

import com.bahadir.pos.exception.JwtTokenException;
import com.bahadir.pos.service.SystemLogService;
import com.bahadir.pos.utils.ApiResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController {

    private final SystemLogService systemLogService;

    public ErrorController(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> handleError(HttpServletRequest request) {
        Throwable exception = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        int statusCode = (int) request.getAttribute("jakarta.servlet.error.status_code");
        HttpStatus httpStatus;

        if (exception instanceof JwtException || exception instanceof JwtTokenException || exception instanceof AccessDeniedException) {
            httpStatus = HttpStatus.UNAUTHORIZED;  // Örneğin, geçersiz argüman hatası
        } else {
            httpStatus = HttpStatus.FORBIDDEN;
        }

        String errorSource = "SECURITY";

        systemLogService.saveLog(request, exception, httpStatus, errorSource);

        ApiResponse<Void> response = ApiResponse.error(httpStatus.value(), exception, errorSource);

        return new ResponseEntity<>(response, httpStatus);
    }
}


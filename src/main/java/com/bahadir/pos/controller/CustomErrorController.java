package com.bahadir.pos.controller;

import com.bahadir.pos.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class CustomErrorController {

    @GetMapping
    public ResponseEntity<ApiResponse<?>> handleError(HttpServletRequest request) {
        Throwable exception = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        int statusCode = (int) request.getAttribute("jakarta.servlet.error.status_code");

        ApiResponse<Void> response = ApiResponse.error(statusCode, (exception != null) ? exception.getMessage() : "Unknown error");
        return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }
}


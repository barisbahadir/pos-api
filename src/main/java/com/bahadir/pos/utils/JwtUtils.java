package com.bahadir.pos.utils;

import jakarta.servlet.http.HttpServletRequest;

public class JwtUtils {

    public static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " sonrası token'ı al
        }
        return null;
    }

}

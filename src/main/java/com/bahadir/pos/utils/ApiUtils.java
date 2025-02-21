package com.bahadir.pos.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ApiUtils {

    public static String getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " sonrası token'ı al
        }
        return null;
    }

    public static String getStackTraceMessage(Throwable exception) {
       if(exception != null && exception.getStackTrace() != null){
           StringBuilder stackTrace = new StringBuilder();
           for (StackTraceElement element : exception.getStackTrace()) {
               stackTrace.append(element.toString()).append("\n ");
           }
           return stackTrace.toString();
       }
       return null;
    }

}

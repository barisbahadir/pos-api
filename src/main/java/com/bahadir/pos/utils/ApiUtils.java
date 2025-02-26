package com.bahadir.pos.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.security.SecureRandom;
import java.util.Random;

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

    public static Long getPathId(String id){
        try{
            return Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateBarcode() {
        Random random = new Random();
        StringBuilder barcode = new StringBuilder();

        // Generate a random 10-digit string
        for (int i = 0; i < 10; i++) {
            int rand = random.nextInt(10); // Random number between 0 and 9
            barcode.append(rand); // Append the digit
        }

        return barcode.toString();
    }

    public static int generateOtpNumber() {
        SecureRandom random = new SecureRandom();
        return 100_000 + random.nextInt(900_000); // 100000 ile 999999 arasında bir sayı üretir
    }
}

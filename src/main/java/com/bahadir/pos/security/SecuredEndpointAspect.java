package com.bahadir.pos.security;

import com.bahadir.pos.utils.ApiResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecuredEndpointAspect {

    @Around("@annotation(securedEndpoint)")
    public Object handleSecuredEndpoint(ProceedingJoinPoint joinPoint, SecuredEndpoint securedEndpoint) throws Throwable {
        // Kullanıcının oturum bilgilerini al
        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        var roles = authentication.getAuthorities().stream()
//                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
//                .toList();

        var roles = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList();

        // Rol kontrolü
        if (!roles.contains(securedEndpoint.role().name())) {
            ApiResponse<Void> response = ApiResponse.error(HttpStatus.FORBIDDEN.value(), "Yetkisiz erisim talebi engellendi. (Mevcut roller: " + roles.toString() + " - Gereken Rol: " + securedEndpoint.role().name() + ")");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Filtreleme
        if (securedEndpoint.filter()) {
            Object[] args = joinPoint.getArgs();
            // Gerekirse argümanlar üzerinde işlem yapılabilir
        }

        // Metodu çalıştır ve sonucu döndür
        return joinPoint.proceed();
    }
}

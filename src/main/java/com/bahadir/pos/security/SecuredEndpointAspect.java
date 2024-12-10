package com.bahadir.pos.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecuredEndpointAspect {

    @Around("@annotation(securedEndpoint)")
    public Object checkSecurityAndFilter(ProceedingJoinPoint joinPoint, SecuredEndpoint securedEndpoint) throws Throwable {
        // Kullanıcı rolünü al
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var roles = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();

        // Role doğrulaması
        if (!roles.contains(securedEndpoint.role())) {
            return ResponseEntity.status(403).body("Yetkisiz erisim! (Kullanici rolu bu servisi kullanmak icin yeterli degil)");
        }

        // Filtreleme işlemi
        if (securedEndpoint.filter()) {
            Object[] args = joinPoint.getArgs();
            // Gerekli argümanları filtrele ve değiştirebilirsiniz
            // Örneğin: Tarih aralığını varsayılan değerlere çekmek
        }

        // Servis metodunu çalıştır
        return joinPoint.proceed();
    }
}


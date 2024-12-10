package com.bahadir.pos.controller;

import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.authentication.AuthenticationRequest;
import com.bahadir.pos.entity.authentication.AuthenticationResponseDto;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.security.JwtTokenProvider;
import com.bahadir.pos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    // Kullanıcı kaydı
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            userService.registerUser(authenticationRequest.getEmail(), authenticationRequest.getPassword());
            return ResponseEntity.ok().body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Email already taken");
        }
    }

    // Kullanıcı girişi (JWT token ile)
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequest authenticationRequest) {

        // Kullanıcıyı email ile bul
        User user = userService.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new ApiException("User not found with email: " + authenticationRequest.getEmail()));

        // Şifreyi kontrol et
        if (!userService.validatePassword(user, authenticationRequest.getPassword())) {
            throw new ApiException("Invalid password");
        }

        // Kullanıcıyı doğruladıktan sonra, AuthenticationManager ile Authentication oluştur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );

        // SecurityContext'te authentication bilgilerini ayarla
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT token oluştur
        String jwt = jwtTokenProvider.generateJwtToken(authenticationRequest.getEmail(), List.of(user.getRole().name()));

        // JWT token'ı döndür
        AuthenticationResponseDto dto = AuthenticationResponseDto
                .builder()
                .email(authenticationRequest.getEmail())
                .role(user.getRole())
                .token(jwt)
                .build();

        return ResponseEntity.ok(dto);
    }
}
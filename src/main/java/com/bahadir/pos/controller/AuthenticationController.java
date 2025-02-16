package com.bahadir.pos.controller;

import com.bahadir.pos.entity.authentication.AuthenticationRequest;
import com.bahadir.pos.entity.authentication.AuthenticationResponseDto;
import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.security.JwtTokenProvider;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.PermissionService;
import com.bahadir.pos.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PermissionService permissionService;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    PermissionService permissionService,
                                    UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.permissionService = permissionService;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> listAll() {
        List<User> users = userService.getAllUsers();
        users.stream()
                .forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // Kullanıcı kaydı
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            userService.registerUser(authenticationRequest.getEmail(), authenticationRequest.getUsername(), authenticationRequest.getPassword());
            return ResponseEntity.ok().body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Email already taken");
        }
    }

    // Kullanıcı girişi (JWT token ile)
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequest authenticationRequest) {

        User user = userService.findByEmailWithDetails(authenticationRequest.getEmail())
                .orElseThrow(() -> new ApiException("User can't found with email: " + authenticationRequest.getEmail()));

        if (!userService.validatePassword(user, authenticationRequest.getPassword())) {
            throw new ApiException("Invalid password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );

        // SecurityContext'te authentication bilgilerini ayarla
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT token oluştur
        String jwt = jwtTokenProvider.generateJwtToken(authenticationRequest.getEmail(), List.of(user.getAuthRole().name()));

        List<Permission> sortedPermissions = new ArrayList<>();

        if (user.getRole().getPermissions() != null) {
            sortedPermissions = permissionService.getSortedPermissions(new ArrayList<>(user.getRole().getPermissions()));
            user.getRole().setPermissions(new LinkedHashSet<>(sortedPermissions));
        }

        AuthenticationResponseDto dto = AuthenticationResponseDto
                .builder()
                .email(authenticationRequest.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .permissions(sortedPermissions)
                .token(jwt)
                .avatar(null)
                .id(user.getId())
                .status(user.getStatus().name())
                .build();

        return ResponseEntity.ok(dto);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        userService.deleteAllUsers();
        return ResponseEntity.ok(true);
    }
}
package com.bahadir.pos.controller;

import com.bahadir.pos.entity.BaseStatus;
import com.bahadir.pos.entity.JwtTokenResponse;
import com.bahadir.pos.entity.authentication.AuthenticationRequest;
import com.bahadir.pos.entity.authentication.AuthenticationResponseDto;
import com.bahadir.pos.entity.authentication.AuthenticationType;
import com.bahadir.pos.entity.permission.Permission;
import com.bahadir.pos.entity.user.User;
import com.bahadir.pos.entity.user.UserRole;
import com.bahadir.pos.exception.ApiException;
import com.bahadir.pos.security.JwtTokenProvider;
import com.bahadir.pos.security.SecuredEndpoint;
import com.bahadir.pos.service.*;
import com.bahadir.pos.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SessionService sessionService;
    private final TwoFactorOtpService twoFactorOtpService;
    private final TwoFactorMailService twoFactorMailService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider,
                                    PermissionService permissionService,
                                    UserService userService,
                                    SessionService sessionService,
                                    TwoFactorOtpService twoFactorOtpService,
                                    TwoFactorMailService twoFactorMailService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.permissionService = permissionService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.twoFactorOtpService = twoFactorOtpService;
        this.twoFactorMailService = twoFactorMailService;
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
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) {

        User user = userService.findByEmailWithDetails(authenticationRequest.getEmail())
                .orElseThrow(() -> new ApiException("User can't found with email: " + authenticationRequest.getEmail()));

        if (!userService.validatePassword(user, authenticationRequest.getPassword())) {
            throw new ApiException("Invalid password");
        }

        if (user.getStatus() == BaseStatus.DISABLE) {
            throw new ApiException("Kullanıcı aktif değildir, lütfen yöneticinizle görüşün.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );

        // SecurityContext'te authentication bilgilerini ayarla
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /* -------------------------- OTP -------------------------- */

        // Eğer 2FA etkinse, ikinci aşama için dönüş yap
        if (user.getAuthType() != AuthenticationType.NONE && authenticationRequest.getAuthValue() == null) {
            AuthenticationResponseDto twoFactorResponse = AuthenticationResponseDto
                    .builder()
                    .authType(user.getAuthType())
                    .isAuthenticated(false)
                    .build();

            if (user.getAuthType() == AuthenticationType.OTP) {
                twoFactorResponse.setTwoFactorQrCode(twoFactorOtpService.generateQrCode(user.getEmail(), user.getTwoFactorAuthSecretKey()));
            } else {
                int emailCode = ApiUtils.generateOtpNumber();
                user.setTwoFactorEmailCode(String.valueOf(emailCode));
                userService.updateUser(user.getId(), user);
                twoFactorMailService.sendOtpEmail(user.getEmail(), emailCode);
            }
            return ResponseEntity.ok(twoFactorResponse);
        }

        // Kullanıcının 2FA türüne göre işlem yap
        if (user.getAuthType() == AuthenticationType.OTP) {
            if (authenticationRequest.getAuthValue() == null) {
                throw new ApiException("Authenticator ile uretilen kodu girmeniz gereklidir.");
            }
            int otpValue = authenticationRequest.getAuthValue().length() == 6 ? Integer.parseInt(authenticationRequest.getAuthValue()) : 0;
            boolean is2faValid = twoFactorOtpService.validateOtp(user.getTwoFactorAuthSecretKey(), otpValue);
            if (!is2faValid) {
                throw new ApiException("Geçersiz OTP doğrulama kodu girdiniz.");
            }
        } else if (user.getAuthType() == AuthenticationType.EMAIL) {
            if (authenticationRequest.getAuthValue() == null) {
                throw new ApiException("E-posta ile gonderilen doğrulamanu kodu girmeniz gereklidir.");
            }
            if (!user.getTwoFactorEmailCode().equals(authenticationRequest.getAuthValue())) {
                throw new ApiException("Geçersiz OTP doğrulama kodu girdiniz.");
            }
        }

        /* -------------------------- OTP -------------------------- */

        // JWT token oluştur
        JwtTokenResponse jwtResponse = jwtTokenProvider.generateJwtToken(user, request);

        List<Permission> sortedPermissions = new ArrayList<>();

        if (user.getRole().getPermissions() != null) {
            sortedPermissions = permissionService.getSortedPermissions(new ArrayList<>(user.getRole().getPermissions()));
            user.getRole().setPermissions(new LinkedHashSet<>(sortedPermissions));
        }

        AuthenticationResponseDto authenticatedRes = AuthenticationResponseDto
                .builder()
                .email(authenticationRequest.getEmail())
                .username(user.getUsername())
                .role(user.getAuthRole())
                .permissions(sortedPermissions)
                .avatar(null)
                .id(user.getId())
                .status(user.getStatus().name())
                .authType(user.getAuthType())
                .isAuthenticated(true)
                .token(jwtResponse.getJwtToken())
                .build();

        return ResponseEntity.ok(authenticatedRes);
    }

    @SecuredEndpoint(role = UserRole.ADMIN, filter = true)
    @GetMapping("/delete/all")
    public ResponseEntity<Boolean> deleteAll() {
        userService.deleteAllUsers();
        return ResponseEntity.ok(true);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request) {

        try {
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();   // Session'ı öldür
        } catch (Exception e) { }

        String token = ApiUtils.getJwtFromRequest(request);
        if (token != null)
            sessionService.closeSessionByToken(token);

        return ResponseEntity.ok(token != null);
    }

}
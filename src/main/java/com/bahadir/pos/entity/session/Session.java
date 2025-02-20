package com.bahadir.pos.entity.session;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.bahadir.pos.entity.user.User;

@Entity
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String username;  // Kullanıcı adı

    @Column(nullable = false)
    private String email;  // Kullanıcı e-posta adresi

    @Column(nullable = false)
    private String userRole;

    @Column(nullable = false)
    private LocalDateTime loginDate;

    @Column(nullable = false)
    private LocalDateTime lastAccessDate;

    private LocalDateTime tokenExpireDate;

    private LocalDateTime logoutDate;

    private String ipAddress;

    private String userAgent;
}
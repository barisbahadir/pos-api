package com.bahadir.pos.entity.session;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  // UUID'yi String olarak saklayacak şekilde
    private String id;

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
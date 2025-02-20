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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    @Column(nullable = false)
    private LocalDateTime lastAccessTime;

    private LocalDateTime logoutTime;

    @Column(nullable = false)
    private String userRole;

    private String ipAddress;

    private String userAgent;
}
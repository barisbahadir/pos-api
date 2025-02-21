package com.bahadir.pos.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class ApiLog {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")  // UUID'yi String olarak saklayacak şekilde
    private String id;

    private String userSessionId;
    private String email;

    private String requestUri;
    private String method;

    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(columnDefinition = "TEXT")
    private String responseBody;
    private int responseStatus;

    @Column(nullable = false)
    private LocalDateTime date;
}
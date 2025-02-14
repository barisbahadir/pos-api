package com.bahadir.pos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static com.bahadir.pos.utils.DateTimeUtils.DEFAULT_DATE_FORMAT;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_FORMAT)
    private LocalDateTime lastUpdatedDate;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now; // Oluşturulma tarihi
        this.lastUpdatedDate = now; // İlk kayıtta lastUpdatedDate de ayarlanır

        this.status = BaseStatus.ENABLE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdatedDate = LocalDateTime.now(); // Güncelleme işleminde lastUpdatedDate set edilir
    }

    // Parametresiz constructor
    public BaseEntity() {
    }
}

package com.bahadir.pos.entity.product;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.category.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product extends BaseEntity {

    private String barcode;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer orderValue;

    @Column(columnDefinition = "TEXT")
    private String image;  // Base64 formatında string

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference  // Kategorinin geri referansı (kategorilerle ilgili gereksiz döngüyü önler)
    private Category category;  // Ürünün kategorisi

    @PrePersist
    protected void onCreate() {
        this.orderValue = 1;

        LocalDateTime now = LocalDateTime.now();
        this.setCreatedDate(now);
        this.setLastUpdatedDate(now);
    }
}

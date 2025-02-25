package com.bahadir.pos.entity.product;

import com.bahadir.pos.entity.BaseEntity;
import com.bahadir.pos.entity.category.Category;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder  // Bu, BaseEntity'deki builder'ı kullanır
@NoArgsConstructor
public class Product extends BaseEntity {

    private String barcode;

    private BigDecimal purchasePrice;
    private Integer taxRate;
    private Integer profitMargin;
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

    @PreUpdate
    protected void onUpdate() {
        this.setLastUpdatedDate(LocalDateTime.now());
    }
}

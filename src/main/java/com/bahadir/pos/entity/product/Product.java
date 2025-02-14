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
    private String name;  // Ürün adı
    private String brand;  // Marka
    private String sku;  // Stok kodu (Stock Keeping Unit)
    private BigDecimal price;
    private BigDecimal discountPrice;  // İndirimli fiyat
    private Integer stockQuantity;
    private Integer orderValue;

    @Column(columnDefinition = "TEXT")
    private String image;  // Base64 formatında string

    @Column(columnDefinition = "TEXT")
    private String description;  // Ürün açıklaması

    @Column(length = 500)
    private String shortDescription;  // Kısa açıklama

    private Double weight;  // Ürün ağırlığı (kg)
    private Double width;  // Ürün genişliği (cm)
    private Double height;  // Ürün yüksekliği (cm)
    private Double depth;  // Ürün derinliği (cm)

    private Boolean isActive;  // Ürün aktif mi?
    private Boolean isFeatured;  // Öne çıkan ürün mü?

    private Integer viewCount;  // Görüntülenme sayısı
    private Integer soldCount;  // Satılan ürün sayısı

    @ElementCollection
    private List<String> tags;  // Ürün etiketleri

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
        this.isActive = true;  // Varsayılan olarak aktif
        this.viewCount = 0;  // Yeni ürün için başlangıç görüntülenme sayısı
        this.soldCount = 0;  // Yeni ürün için başlangıç satılma sayısı
    }

    @PreUpdate
    protected void onUpdate() {
        this.setLastUpdatedDate(LocalDateTime.now());
    }
}
